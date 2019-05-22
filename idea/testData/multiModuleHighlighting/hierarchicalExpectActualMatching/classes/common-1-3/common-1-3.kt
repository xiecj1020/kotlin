package sample

expect data class A5(<!EXPECTED_CLASS_CONSTRUCTOR_PROPERTY_PARAMETER!>val x: Int<!>)

expect data class A6<R>(<!EXPECTED_CLASS_CONSTRUCTOR_PROPERTY_PARAMETER!>val x: R<!>)

expect sealed class A7

expect sealed class A8<T>

expect open class A9<T> where T : Comparable<T>
