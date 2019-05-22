package sample

actual val <T> T.x1 by lazy { 10 <!UNCHECKED_CAST("Int", "T")!>as T<!> }

actual val x1: List<Int> get() = listOf(1)

actual val x2: Nothing = null!!

actual val <T> MutableList<out T>.x2 get() = null <!UNCHECKED_CAST("Nothing?", "T")!>as T<!>

actual var <T> Map<in T, <!REDUNDANT_PROJECTION("Map")!>out<!> T>.x2
    get() = null <!UNCHECKED_CAST("Nothing?", "T")!>as T<!>
    set(<!UNUSED_PARAMETER("value")!>value<!>) {}

actual inline var Number.x2
    get() = 10
    set(<!UNUSED_PARAMETER("value")!>value<!>) {}

actual inline val <T> T.x3 get() = 10 <!UNCHECKED_CAST("Int", "T")!>as T<!>
