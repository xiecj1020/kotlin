package sample

expect val <T> T.x1: T

expect val x1: List<Int>

expect val x2: Nothing

expect val <T> MutableList<out T>.x2: T

expect var <T> Map<in T, <!REDUNDANT_PROJECTION("Map")!>out<!> T>.x2: T

expect var Number.x2: Int

// Unexpected behaviour
<!INLINE_PROPERTY_WITH_BACKING_FIELD!>expect inline val <T> T.x3: T<!>

expect var x4: Int

expect val <T> T.x5: T

expect val x6: Int

interface I

expect val x7: I

expect var <T> T.x8: I

expect val x9: I

expect val x10: Number
