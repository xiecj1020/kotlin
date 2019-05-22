package sample

expect class A13<T>

expect class A14<T> : Iterable<T> {
    override fun iterator(): Nothing
}

expect class A15<T> : Iterable<T> {
    override fun iterator(): Nothing
}

expect <!EXPERIMENTAL_FEATURE_WARNING("The feature "inline classes" is experimental")!>inline<!> class A22<T>(val x: Int): Comparable<T>

expect class A23<!NO_ACTUAL_FOR_EXPECT("class 'A23'", "js for JS", " The following declaration is incompatible because upper bounds of type parameters are different:     public final actual class A23<T : Number> "), NO_ACTUAL_FOR_EXPECT("class 'A23'", "native for Native", " The following declaration is incompatible because upper bounds of type parameters are different:     public final actual class A23<T : Number> ")!><T : <!FINAL_UPPER_BOUND("Int")!>Int<!>><!>() {
    inner class A24<K : T> {

    }
}
