package sample

actual class A3<K> : Iterable<K> {
    override fun iterator() = null!!
}

actual enum class A4

actual enum class A41 {
    TEST
}

expect abstract class A10<T : Iterable<<!REDUNDANT_PROJECTION("Iterable")!>out<!> T>>

expect interface A11<T, K> : Comparable<K> where K: T, T: Iterable<K>

expect object A12 : Comparable<Int>
