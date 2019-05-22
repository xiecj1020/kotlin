package sample

actual val <T> T.x1 by lazy { 10 <!UNCHECKED_CAST("Int", "T")!>as T<!> }

actual val x1: List<Int> get() = listOf(1)
