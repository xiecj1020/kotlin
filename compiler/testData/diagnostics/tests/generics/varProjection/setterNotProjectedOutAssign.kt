// !LANGUAGE: +NewInference
// !CHECK_TYPE
// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER -UNUSED_EXPRESSION
interface Tr<T> {
    var v: Tr<T>
}

//fun test(t: Tr<*>) {
//    <!SETTER_PROJECTED_OUT!>t.v<!> = t
//    t.v checkType { _<Tr<*>>() }
//}

interface Inv<T>

class InvImpl<T> : Inv<T>

class A<T> {
    fun <F : Inv<T>> foo(x: F) = 1
}

class B<T> {
    fun foo(x: T) = 1
}

fun test2(a: A<out CharSequence>, b: B<out CharSequence>) {
    a.foo(InvImpl())
//    b.foo("")
}