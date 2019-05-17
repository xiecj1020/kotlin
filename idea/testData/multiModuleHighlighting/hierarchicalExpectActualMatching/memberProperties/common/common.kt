package sample

expect class A1

expect class A2<T>

expect class A3<T> : Iterable<T>

expect enum class A4

expect data class A5(<!EXPECTED_CLASS_CONSTRUCTOR_PROPERTY_PARAMETER!>val x: Int<!>)

expect data class A6<R>(<!EXPECTED_CLASS_CONSTRUCTOR_PROPERTY_PARAMETER!>val x: R<!>)

expect sealed class A7

expect sealed class A8<T>

expect open class A9<T> where T : Comparable<T>

expect abstract class A10<T : Iterable<<!REDUNDANT_PROJECTION("Iterable")!>out<!> T>>

expect interface A11<T, K> : Comparable<K> where K: T, T: Iterable<K>

expect object A12 : Comparable<Int>

expect class A13<T>

expect class A14<T> : Iterable<T> {
    override fun iterator(): Nothing
}

expect class A15<T> : Iterable<T> {
    override fun iterator(): Nothing
}

expect class A16<T> : Iterable<T>

expect class A17<T> : Iterable<T>

expect annotation class A18

expect annotation class A19 {
    annotation class A20(val x: Int) {
        sealed class A21<T> : Iterable<T>, Comparable<T> {
            val x: T
        }
    }
}

expect <!EXPERIMENTAL_FEATURE_WARNING("The feature "inline classes" is experimental")!>inline<!> class A22<T>(val x: Int): Comparable<T>

expect inline class A23<T : Int>(val x: Int) {
    inner class A24<K : T> {

    }
}