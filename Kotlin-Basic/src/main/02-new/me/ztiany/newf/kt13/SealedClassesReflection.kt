package me.ztiany.newf.kt13

/** We've added a new API to kotlin-reflect that can be used to enumerate all the direct subtypes of a sealed class, namely KClass.sealedSubclasses. */
fun main() {
    println("ReflectionSealedClasses::class.sealedSubclasses: ${ReflectionSealedClasses::class.sealedSubclasses}")
}

private sealed class ReflectionSealedClasses
private class ReflectionSealedClassesSub1 : ReflectionSealedClasses()
private class ReflectionSealedClassesSub2 : ReflectionSealedClasses()
