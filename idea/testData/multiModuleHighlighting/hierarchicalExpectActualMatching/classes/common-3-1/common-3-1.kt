package sample

expect annotation class A18

expect annotation class A19 {
    annotation class A20(val x: Int) {
        sealed class A21<T> : Iterable<T>, Comparable<T> {
            val x: T
        }
    }
}

actual enum class A42 {
    TEST
}

actual enum class <!ACTUAL_WITHOUT_EXPECT("Actual enum class 'A43'", " The following declaration is incompatible because some entries from expected enum are missing in the actual enum:     public final expect enum class A43 : Enum<A43> ")!>A43<!> {
    TEST2
}

actual enum class A44<!ACTUAL_MISSING!>(<!UNUSED_PARAMETER("x")!>x<!>: Int)<!> {
    TEST(1)
}