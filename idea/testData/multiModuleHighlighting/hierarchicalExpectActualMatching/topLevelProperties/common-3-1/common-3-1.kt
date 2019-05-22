package sample

expect var <T> Map<in T, <!REDUNDANT_PROJECTION("Map")!>out<!> T>.x2: T

expect var Number.x2: Int

// unexpected behaviour: KT-31464
<!INLINE_PROPERTY_WITH_BACKING_FIELD!>expect inline val <T> T.x3: T<!>
