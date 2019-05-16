package sample

expect fun x1(): List<Int>

expect fun x2(): Nothing

expect fun <T> MutableList<out T>.x2(): T

expect fun <T> Map<in T, <!REDUNDANT_PROJECTION("Map")!>out<!> T>.x2(): T

expect fun Number.x2(): Int

expect inline fun <T> T.x3(): T

expect fun x4(): Int

expect fun <T> T.x5(): T

expect fun x6(): Int

interface I

expect fun x7(): I

expect fun <T> T.x8(): I

expect fun x9(): <!NO_ACTUAL_FOR_EXPECT("function 'x9'", "jvm for JVM", " The following declaration is incompatible because return type is different:     public actual fun x9(): Case9 ")!>I<!>

expect fun x10(): <!NO_ACTUAL_FOR_EXPECT("function 'x10'", "jvm for JVM", " The following declaration is incompatible because return type is different:     public actual fun x10(): Int ")!>Number<!>

expect suspend fun x11(): Int

expect suspend inline fun <T> (suspend T.(T) -> T).x12(crossinline x: (T) -> T): T

expect suspend inline fun <T> (T.(T) -> T).x13<!NO_ACTUAL_FOR_EXPECT("function 'x13'", "jvm for JVM", " The following declaration is incompatible because parameter types are different:     public actual suspend inline fun <T> (suspend T.(T) -> T).x13(crossinline x: (T) -> T): T ")!>(crossinline x: (T) -> T)<!>: T

expect suspend inline fun <T> (suspend T.(T) -> T).x14(crossinline x: (T) -> T): T

expect suspend fun <T> (suspend T.(T) -> T).x15(x: (T) -> T): T

expect tailrec fun x16(y: () -> Unit): Int

expect infix fun <T> T.x17(x: Int): Int

expect infix fun <T> T.x18(x: Int): Int

expect fun <T> T.x19(x: Int): Int

expect operator fun CharSequence.plus(x: Int): Int

expect internal suspend inline infix operator fun <T> T.plus(x: () -> T): T

expect infix operator fun <T> T.minus(x: () -> T): T

expect fun x20(): Int

expect internal fun x21(): Int
