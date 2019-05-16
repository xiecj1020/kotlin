package sample

expect class Z<T, K>

open expect class A {
    internal val test: Int
    internal class B(x: Any = object {}) {
        internal interface C {}
    }
}

package sample

@Target(AnnotationTarget.TYPE)
annotation class Anno

actual typealias Z<@Anno reified K, @Anno reified T> = @Anno V.P<@Anno K, T>

class V {
    inner class P<T, K> where T: Any? {

    }
}

open actual class A constructor(actual public val test: Int) {
    internal actual class B actual constructor(x: Any) {
        init {}
        internal actual interface C {}
    }
}

/* ---------- */

package sample

expect inline fun <reified T, reified K: T, P: K, reified S: P> Map<T, P>.x()

actual inline fun <reified T, reified K: T, P: K, reified S: P> Map<T, P>.x() {

}

/* ---------- */

package sample

expect fun <T> List<T>.x()

actual inline fun <reified T> List<T>.x() {

}

/* ---------- */

expect val <T> T.x: T

actual val <T> T.x by lazy { 10 <!UNCHECKED_CAST("Int", "T")!>as T<!> }

/* ---------- */

expect val x: List<Int>

actual val x: List<Int> get() = listOf(1)

/* ---------- */

expect val x: Nothing

actual val x = throw Exception()

/* ---------- */

expect val <T> MutableList<out T>.x: T

actual val <T> MutableList<out T>.x = mutableListOf(null as T)
