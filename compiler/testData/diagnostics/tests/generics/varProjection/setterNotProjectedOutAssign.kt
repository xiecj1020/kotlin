// !LANGUAGE: +NewInference
// !CHECK_TYPE
// !WITH_NEW_INFERENCE
//interface Tr<T> {
//    var v: Tr<T>
//}
//
//fun test(t: Tr<*>) {
//    <!SETTER_PROJECTED_OUT!>t.v<!> = t
//    t.v checkType { _<Tr<*>>() }
//}

class Out<out X>
class In<in Y>
class Inv<Z>

class A<T> {
    fun <E : Out<T>> foo1(x: E) = 1
    fun <F : Inv<T>> foo2(x: F) = 1
    fun <G : In<T>>  foo3(x: G) = 1
}

fun foo2(a: A<out CharSequence>, b: A<in CharSequence>) {
    a.foo2(Inv())
}