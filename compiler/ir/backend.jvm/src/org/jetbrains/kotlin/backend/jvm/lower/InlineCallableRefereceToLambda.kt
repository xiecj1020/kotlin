/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.jvm.lower

import org.jetbrains.kotlin.backend.common.FileLoweringPass
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.ScopeWithIr
import org.jetbrains.kotlin.backend.common.ir.copyTypeParametersFrom
import org.jetbrains.kotlin.backend.common.ir.copyValueParametersToStatic
import org.jetbrains.kotlin.backend.common.ir.isInlineParameter
import org.jetbrains.kotlin.backend.common.ir.valueParameter
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irBlock
import org.jetbrains.kotlin.backend.common.phaser.makeIrFilePhase
import org.jetbrains.kotlin.backend.jvm.JvmBackendContext
import org.jetbrains.kotlin.backend.jvm.JvmLoweredDeclarationOrigin
import org.jetbrains.kotlin.backend.jvm.codegen.isInlineFunctionCall
import org.jetbrains.kotlin.backend.jvm.codegen.isInlineIrExpression
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionReferenceImpl
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.Name

internal val inlineCallableReferenceToLambdaPhase = makeIrFilePhase(
    ::InlineCallableReferenceToLambdaPhase,
    name = "InlineCallableReferenceToLambdaPhase",
    description = "Support callable references inlining"
)


internal class InlineCallableReferenceToLambdaPhase(val context: JvmBackendContext) : FileLoweringPass {
    private val inlinableCR = mutableSetOf<IrCallableReference>()
    override fun lower(irFile: IrFile) {
        irFile.transformChildrenVoid(object : IrElementTransformerVoidWithContext() {

            override fun visitFunctionAccess(expression: IrFunctionAccessExpression): IrExpression {
                val callee = expression.symbol.owner
                if (callee.isInlineFunctionCall(context)) {
                    callee.valueParameters.forEach { valueParameter ->
                        if (valueParameter.isInlineParameter()) {
                            expression.getValueArgument(valueParameter.index)?.let { argument ->
                                if (argument is IrCallableReference && isInlineIrExpression(argument)) {
                                    inlinableCR.add(argument)
                                }
                            }
                        }
                    }
                }

                return super.visitFunctionAccess(expression)
            }


            override fun visitPropertyReference(expression: IrPropertyReference): IrExpression {
                if (inlinableCR.contains(expression)) {

                    //use field if present...
                    expression.field?.let { it ->
                        val irBuilder =
                            context.createIrBuilder(currentScope!!.scope.scopeOwnerSymbol, expression.startOffset, expression.endOffset)
                        val field = it.owner

                        return irBuilder.irBlock(expression, IrStatementOrigin.LAMBDA) {
                            val newLambda = buildFun {
                                setSourceRange(expression)
                                origin = JvmLoweredDeclarationOrigin.FUNCTION_REFERENCE_IMPL
                                name = Name.identifier("stub_for_inline")
                                visibility = Visibilities.LOCAL
                                returnType = field.type
                                isSuspend = false
                            }.apply {

                                val receiver = when {
                                    field.isStatic -> null
                                    else -> this.valueParameter(0, Name.identifier("receiver"), field.parentAsClass.defaultType)
                                }
                                receiver?.let {
                                    valueParameters.add(it)
                                }

                                val lambdaBodyBuilder = this@InlineCallableReferenceToLambdaPhase.context.createIrBuilder(this.symbol)
                                body = lambdaBodyBuilder.irBlockBody(startOffset, endOffset) {
                                    +irReturn(
                                        irGetField(receiver?.let { irGet(valueParameters[0]) }, field)
                                    )
                                }
                            }
                            +newLambda

                            +IrFunctionReferenceImpl(
                                expression.startOffset, expression.endOffset, field.type,
                                newLambda.symbol, newLambda.symbol.descriptor, 0,
                                IrStatementOrigin.LAMBDA
                            )
                        }
                    }

                    //..else use getter
                    return functionReferenceToLambda(currentScope!!, expression, expression.getter!!.owner)
                }
                return super.visitPropertyReference(expression)
            }

            override fun visitFunctionReference(expression: IrFunctionReference): IrExpression {
                if (inlinableCR.contains(expression)) {
                    val referencedFunction = expression.symbol.owner
                    return functionReferenceToLambda(currentScope!!, expression, referencedFunction)
                }

                return super.visitFunctionReference(expression)
            }
        })
    }

    private fun functionReferenceToLambda(
        scope: ScopeWithIr,
        expression: IrCallableReference,
        referencedFunction: IrFunction
    ): IrExpression {
        val irBuilder =
            context.createIrBuilder(scope.scope.scopeOwnerSymbol, expression.startOffset, expression.endOffset)

        return irBuilder.irBlock(expression, IrStatementOrigin.LAMBDA) {
            val newLambda = buildFun {
                setSourceRange(expression)
                origin = JvmLoweredDeclarationOrigin.FUNCTION_REFERENCE_IMPL
                name = Name.identifier("stub_for_inline")
                visibility = Visibilities.LOCAL
                returnType = referencedFunction.returnType
                isSuspend = false
            }.apply {
                if (referencedFunction is IrConstructor) {
                    copyTypeParametersFrom(referencedFunction.parentAsClass)
                }
                copyTypeParametersFrom(referencedFunction)
                copyValueParametersToStatic(referencedFunction, origin)
                val lambdaBodyBuilder = this@InlineCallableReferenceToLambdaPhase.context.createIrBuilder(this.symbol)
                body = lambdaBodyBuilder.irBlockBody(startOffset, endOffset) {
                    var shift = 0
                    +irReturn(
                        irCall(referencedFunction.symbol).also { call ->

                            this@apply.typeParameters.forEach {
                                call.putTypeArgument(it.index, expression.getTypeArgument(it.index))
                            }

                            referencedFunction.dispatchReceiverParameter?.let {
                                call.dispatchReceiver = irGet(valueParameters[shift++])
                            }
                            referencedFunction.extensionReceiverParameter?.let {
                                call.extensionReceiver = irGet(valueParameters[shift++])
                            }
                            referencedFunction.valueParameters.indices.forEach {
                                call.putValueArgument(it, irGet(valueParameters[shift++]))
                            }
                        }
                    )
                }
            }
            +newLambda

            +IrFunctionReferenceImpl(
                expression.startOffset, expression.endOffset, referencedFunction.returnType,
                newLambda.symbol, newLambda.symbol.descriptor, referencedFunction.typeParameters.size,
                IrStatementOrigin.LAMBDA
            )
        }
    }
}