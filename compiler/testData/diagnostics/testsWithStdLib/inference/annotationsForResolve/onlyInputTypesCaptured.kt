// !WITH_NEW_INFERENCE
// !LANGUAGE: +NewInference
// !DIAGNOSTICS: -UNUSED_VARIABLE
// Issue: KT-26698

class Foo<T>

class Class<T>

fun test(clazz: Class<*>, list: List<Class<Int>>) {
    val b = clazz in list // Should be OK
}