package sample

// unexpected behaviour: KT-18565
actual data class A5<!ACTUAL_MISSING!>(actual val x: Int)<!>

// unexpected behaviour: KT-18565
actual data class A6<T><!ACTUAL_WITHOUT_EXPECT("Constructor of 'A6'", " The following declaration is incompatible because names of type parameters are different:     public constructor A6<R>(x: R) ")!>(actual val x: T)<!>

actual sealed class A7

actual sealed class A8<R>

actual open class A9<T> where T : Comparable<T>


actual class A13<T : Any?>

actual class A14<T> : Iterable<T> {
    actual override fun iterator(): Nothing = null!!
}

actual class A15<T> : Iterable<T> {
    override fun <!ACTUAL_MISSING!>iterator<!>(): Nothing = null!!
}

actual <!ABSTRACT_MEMBER_NOT_IMPLEMENTED("Class 'A16'", "public abstract operator fun iterator(): Iterator<T> defined in kotlin.collections.Iterable")!>class A16<!><T> : Iterable<T>

actual class A17<T> : Iterable<T> {
    actual override fun <!ACTUAL_WITHOUT_EXPECT("Actual function 'iterator'", "")!>iterator<!>(): Nothing = <!UNRESOLVED_REFERENCE("TODO")!>TODO<!>()
}

actual annotation class <!ACTUAL_WITHOUT_EXPECT("Actual annotation class 'A18'", "")!>A18<!>

actual annotation class <!ACTUAL_WITHOUT_EXPECT("Actual annotation class 'A19'", "")!>A19<!> {
    actual annotation class <!ACTUAL_WITHOUT_EXPECT("Actual annotation class 'A20'", "")!>A20<!>(actual val <!ACTUAL_WITHOUT_EXPECT("Actual property 'x'", "")!>x<!>: Int = 10) {
        actual sealed class <!ACTUAL_WITHOUT_EXPECT("Actual class 'A21'", "")!>A21<!><T> : Iterable<T>, Comparable<T> {
        actual val <!ACTUAL_WITHOUT_EXPECT("Actual property 'x'", "")!>x<!>: T = null <!UNCHECKED_CAST("Nothing?", "T")!>as T<!>
    }
    }
}

actual <!EXPERIMENTAL_FEATURE_WARNING("The feature "inline classes" is experimental")!>inline<!> class A22<T>(actual val x: Int): Comparable<T> {
    override fun compareTo(other: T) = null!!
}

// unexpected behaviour: KT-31498
actual class A23<!ACTUAL_WITHOUT_EXPECT("Actual class 'A23'", " The following declaration is incompatible because upper bounds of type parameters are different:     public final expect class A23<T : Int> ")!><T : Number><!>() {
    actual inner class A24<!ACTUAL_WITHOUT_EXPECT("Actual class 'A24'", " The following declaration is incompatible because upper bounds of type parameters are different:     public final expect inner class A24<K : T> ")!><K : T><!> {

    }
}

actual class A1

actual class A2<K>

expect class A16<T> : Iterable<T>

expect class A17<T> : Iterable<T>

actual enum class A42 {
    TEST
}

actual enum class <!ACTUAL_WITHOUT_EXPECT("Actual enum class 'A43'", " The following declaration is incompatible because some entries from expected enum are missing in the actual enum:     public final expect enum class A43 : Enum<A43> ")!>A43<!> {
    TEST2
}

actual enum class A44<!ACTUAL_MISSING!>(<!UNUSED_PARAMETER("x")!>x<!>: Int)<!> {
    TEST(1)
}

actual class A3<K> : Iterable<K> {
    override fun iterator() = null!!
}

actual enum class A4

actual enum class A41 {
    TEST
}