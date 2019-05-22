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

actual var x4 = 10

// unexpected behaviour
actual <!ACTUAL_WITHOUT_EXPECT("Actual property 'x5'", " The following declaration is incompatible because property kinds are different (val vs var):     public expect val <T> T.x5: T ")!>var<!> <T> T.x5
    get() = null <!UNCHECKED_CAST("Nothing?", "T")!>as T<!>
    set(<!UNUSED_PARAMETER("value")!>value<!>) {}

actual val x6 = 10
