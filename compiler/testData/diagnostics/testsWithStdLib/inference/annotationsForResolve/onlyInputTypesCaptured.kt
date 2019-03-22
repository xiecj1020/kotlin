// !WITH_NEW_INFERENCE
// !LANGUAGE: -NewInference
// !DIAGNOSTICS: -UNUSED_VARIABLE -UNUSED_PARAMETER
// Issue: KT-26698

//class Foo<T>
//
//class Class<T>
//
//fun test(clazz: Class<*>, list: List<Class<Int>>) {
//    val b = clazz in list // Should be OK
//}

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
public fun <@kotlin.internal.OnlyInputTypes T> Iterable<T>.contains1(element: T): Boolean = null!!

@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
public fun <@kotlin.internal.OnlyInputTypes T> Iterable<T>.foo(element: T): T = null!!

class Inv<T>
class Inv2<T, R>

//fun test_2(x: Inv<in Number>, list: List<Inv<Number>>) {
//    list.contains1(x)
//}

//fun test_3(x: Inv<in Number>, list: List<Inv<Int>>) {
//    list.<!TYPE_INFERENCE_ONLY_INPUT_TYPES!>contains1<!>(x)
//}

fun test_4(x: Inv2<in Number, out Number>, list: List<Inv2<Any, Int>>) {
    <!DEBUG_INFO_EXPRESSION_TYPE("Inv2<out kotlin.Any?, out kotlin.Number>")!>list.<!NI;TYPE_INFERENCE_ONLY_INPUT_TYPES!>foo<!>(x)<!>
}