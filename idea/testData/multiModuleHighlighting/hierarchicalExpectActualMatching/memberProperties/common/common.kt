package sample

expect class A1 {
    val x: Number
}

expect class A2<T> {
    var x: T
}

expect class A3<T> : Iterable<T> {
    override fun iterator(): Nothing
    protected val y: T
}

expect enum class A4 {
    ;
    val x: Int
}

expect enum class A5 {
    ;
    val x: Int
}

expect enum class A6 {
    ;
    val x: Int
}

expect sealed class A7 {
    <!EXPECTED_LATEINIT_PROPERTY!>lateinit<!> var x: Any
}

expect sealed class A8 {
    var x: Any
}

expect object A12 : Comparable<Int> {
    <!EXPECTED_DECLARATION_WITH_BODY!>override fun compareTo(other: Int)<!> = <!UNRESOLVED_REFERENCE("TODO")!>TODO<!>()
    <!CONST_VAL_WITHOUT_INITIALIZER!>const<!> val x: Int
}
