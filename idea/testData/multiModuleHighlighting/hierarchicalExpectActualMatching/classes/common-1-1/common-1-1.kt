package sample

expect class A1

expect class A2<T>

expect class A3<T> : Iterable<T>

expect enum class A4

expect enum class A41 {
    TEST
}

expect enum class A42 {
    TEST
}

expect enum class A43 {
    TEST
}

expect enum class A44<!EXPECTED_ENUM_CONSTRUCTOR!>(x: Int)<!> {
    TEST
}
