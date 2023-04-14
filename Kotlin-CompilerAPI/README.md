# Kotlin Compiler API Learning.

The first time you open this project, you will see a gradle sync error. because the kotlin compiler api is not in the local maven repository.

how to fix the error:

1. open the build.gradle file in the root directory of the project. and delete the following code temporarily:

```kotlin
plugins {
    kotlin("jvm") version "1.8.0" apply false
    kotlin("kapt") version "1.8.20" apply false
    //delete the following code
    id("me.ztiany.kcp.plugin.demo") version "1.0.1" apply false
    id("me.ztiany.kcp.plugin.debuglog") version "1.0.1" apply false
}
```

2. publish the all the plugins and libraries to the local maven repository. they are:

    - gradle-plugin: kcp-plugin-group
    - kotlin-compiler-plugin: kcp-plugin-kotlin-debuglog
    - annotation library: kcp-annotation-debuglog

3. then recover the deleted code in the build.gradle file.

---

**kapt resources**:

- [kapt Overview](https://kotlinlang.org/docs/kapt.html)

**kcp resources**:

- [KotlinConf 2018 - Writing Your First Kotlin Compiler Plugin by Kevin Most](https://www.youtube.com/watch?v=w-GMlaziIyo)
- Writing Your Second Kotlin Compiler Plugin series：
   - [Part 1 - Project Setup](https://blog.bnorm.dev/writing-your-second-compiler-plugin-part-1)
   - [Part 2 - Inspecting Kotlin IR](https://blog.bnorm.dev/writing-your-second-compiler-plugin-part-2)
   - [Part 3 - Navigating Kotlin IR](https://blog.bnorm.dev/writing-your-second-compiler-plugin-part-3)
   - [Part 4 - Building Kotlin IR](https://blog.bnorm.dev/writing-your-second-compiler-plugin-part-4)
   - [Part 5 - Transforming Kotlin IR](https://blog.bnorm.dev/writing-your-second-compiler-plugin-part-5)
   - [Part 6 - Support Libraries, Publishing, and Integration Testing](https://blog.bnorm.dev/writing-your-second-compiler-plugin-part-6)

**ksp resources**:

- [KSP Overview](https://kotlinlang.org/docs/ksp-overview.html)
