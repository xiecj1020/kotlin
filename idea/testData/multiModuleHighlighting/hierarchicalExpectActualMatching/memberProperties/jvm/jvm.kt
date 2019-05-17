package sample

actual class A1

actual class A2<K>

actual class A3<K> : Iterable<K> {
    override fun iterator() = TODO()
}

actual enum class A4

// unexpected behaviour
actual data class A5<!ACTUAL_MISSING!>(actual val x: Int)<!>

// unexpected behaviour
actual data class A6<T><!ACTUAL_WITHOUT_EXPECT("Constructor of 'A6'", " The following declaration is incompatible because names of type parameters are different:     public constructor A6<R>(x: R) ")!>(actual val x: T)<!>

actual sealed class A7

actual sealed class A8<R>

actual open class A9<T> where T : Comparable<T>

actual abstract class A10<T : Iterable<<!REDUNDANT_PROJECTION("Iterable")!>out<!> T>>

actual interface A11<T, K> : Comparable<K> where K: T, T: Iterable<K>

actual object A12 : Comparable<Int> {
    override fun compareTo(other: Int) = TODO()
}

actual class A13<T : Any?>

actual class A14<T> : Iterable<T> {
    actual override fun iterator(): Nothing = TODO()
}

actual class A15<T> : Iterable<T> {
    override fun <!ACTUAL_MISSING!>iterator<!>(): Nothing = TODO()
}

actual <!ABSTRACT_MEMBER_NOT_IMPLEMENTED("Class 'A16'", "public abstract operator fun iterator(): Iterator<T> defined in kotlin.collections.Iterable")!>class A16<!><T> : Iterable<T>

actual class A17<T> : Iterable<T> {
    actual override fun <!ACTUAL_WITHOUT_EXPECT("Actual function 'iterator'", "")!>iterator<!>(): Nothing = TODO()
}

actual annotation class A18

actual annotation class A19 {
    actual annotation class A20(actual val x: Int = 10) {
        actual sealed class A21<T> : Iterable<T>, Comparable<T> {
            actual val x: T = null <!UNCHECKED_CAST("Nothing?", "T")!>as T<!>
        }
    }
}

actual <!EXPERIMENTAL_FEATURE_WARNING("The feature "inline classes" is experimental")!>inline<!> class A22<T>(actual val x: Int): Comparable<T> {
    override fun compareTo(other: T) = TODO()
}

actual <!EXPERIMENTAL_FEATURE_WARNING("The feature "inline classes" is experimental")!>inline<!> class <!NO_ACTUAL_CLASS_MEMBER_FOR_EXPECTED_CLASS("A23", "      public final expect inner class A24<K : T>      The following declaration is incompatible because upper bounds of type parameters are different:         public final inner class A24<K : T> ")!>A23<!><T : <!FINAL_UPPER_BOUND("Int")!>Int<!>>(val x: Int) {
    actual inner class A24<K : T> {
    }
}