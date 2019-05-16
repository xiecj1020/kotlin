package sample

actual fun x1(): List<Int> = listOf(1)

actual fun x2(): Nothing = null!!

actual fun <T> MutableList<out T>.x2() = null <!UNCHECKED_CAST("Nothing?", "T")!>as T<!>

actual fun <T> Map<in T, <!REDUNDANT_PROJECTION("Map")!>out<!> T>.x2() = null <!UNCHECKED_CAST("Nothing?", "T")!>as T<!>

actual <!NOTHING_TO_INLINE("public actual inline fun Number.x2(): Int defined in sample in file jvm.kt")!>inline<!> fun Number.x2() = 10

actual <!NOTHING_TO_INLINE("public actual inline fun <T> T.x3(): T defined in sample in file jvm.kt")!>inline<!> fun <T> T.x3() = 10 <!UNCHECKED_CAST("Int", "T")!>as T<!>

actual fun x4() = 10

actual inline fun <!ACTUAL_WITHOUT_EXPECT("Actual function 'x5'", " The following declaration is incompatible because some type parameter is reified in one declaration and non-reified in the other:     public expect fun <T> T.x5(): T ")!><reified T><!> T.x5() = null as T

actual fun x6() = 10

actual fun x7() = object : I {}

actual <!NOTHING_TO_INLINE("public actual inline fun <T> T.x8(): I defined in sample in file jvm.kt")!>inline<!> fun <T> T.x8() = object : I {}

class Case9 : I {}

actual fun <!ACTUAL_WITHOUT_EXPECT("Actual function 'x9'", " The following declaration is incompatible because return type is different:     public expect fun x9(): I ")!>x9<!>() = Case9()

actual fun <!ACTUAL_WITHOUT_EXPECT("Actual function 'x10'", " The following declaration is incompatible because return type is different:     public expect fun x10(): Number ")!>x10<!>() = 10

actual suspend fun x11() = 10

actual suspend inline fun <T> (suspend T.(T) -> T).x12(crossinline x: (T) -> T): T = x(<!NO_VALUE_FOR_PARAMETER("p1")!>)<!>

actual suspend inline fun <T> (suspend T.(T) -> T).x13<!ACTUAL_WITHOUT_EXPECT("Actual function 'x13'", " The following declaration is incompatible because parameter types are different:     public expect suspend inline fun <T> (T.(T) -> T).x13(crossinline x: (T) -> T): T ")!>(crossinline x: (T) -> T)<!>: T = x(<!NO_VALUE_FOR_PARAMETER("p1")!>)<!>

actual suspend inline fun <T> (suspend T.(T) -> T).x14<!ACTUAL_WITHOUT_EXPECT("Actual function 'x14'", " The following declaration is incompatible because some value parameter is noinline in one declaration and not noinline in the other:     public expect suspend inline fun <T> (suspend T.(T) -> T).x14(crossinline x: (T) -> T): T ")!>(noinline x: (T) -> T)<!>: T = x(<!NO_VALUE_FOR_PARAMETER("p1")!>)<!>

<!ACTUAL_WITHOUT_EXPECT("Actual function 'x15'", " The following declaration is incompatible because modifiers are different (suspend):     public expect suspend fun <T> (suspend T.(T) -> T).x15(x: (T) -> T): T ")!>actual<!> fun <T> (suspend T.(T) -> T).x15(x: (T) -> T): T = x(<!NO_VALUE_FOR_PARAMETER("p1")!>)<!>

actual tailrec fun x16(y: () -> Unit): Int {
    y()
    return if (true) 10 else x16 { }
}

actual infix fun <T> T.x17(x: Int) = 10

<!ACTUAL_WITHOUT_EXPECT("Actual function 'x18'", " The following declaration is incompatible because some modifiers on expected declaration are missing on the actual one (external, infix, inline, operator, tailrec):     public expect infix fun <T> T.x18(x: Int): Int ")!>actual<!> fun <T> T.x18(x: Int) = 10

actual infix fun <T> T.x19(x: Int) = 10

actual operator fun CharSequence.plus(x: Int) = 10

actual internal suspend inline infix operator fun <T> T.plus(x: () -> T) = x()

<!ACTUAL_WITHOUT_EXPECT("Actual function 'minus'", " The following declaration is incompatible because some modifiers on expected declaration are missing on the actual one (external, infix, inline, operator, tailrec):     public expect infix operator fun <T> T.minus(x: () -> T): T ")!>actual operator<!> fun <T> T.minus(x: () -> T) = x()

actual <!ACTUAL_WITHOUT_EXPECT("Actual function 'x20'", " The following declaration is incompatible because visibility is different:     public expect fun x20(): Int ")!>internal<!> fun x20() = 10

actual fun x21() = 10
