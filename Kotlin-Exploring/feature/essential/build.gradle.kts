// refer to <https://kotlinlang.org/docs/gradle-compiler-options.html#target-the-jvm> for more details.
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.0.0"

    // https://kotlinlang.org/docs/all-open-plugin.html
    //kotlin("plugin.allopen") version "2.0.0"

    // https://kotlinlang.org/docs/no-arg-plugin.html
    //kotlin("plugin.noarg") version "2.0.0"

    // https://github.com/Kotlin/kotlinx.atomicfu
    // ?

    // https://kotlinlang.org/docs/serialization.html
    //kotlin("plugin.serialization") version "2.0.0"

    // https://kotlinlang.org/docs/ksp-quickstart.html
    //id("com.google.devtools.ksp") version "2.0.0-1.0.21"
}

dependencies{
    //================================================================================
    // test
    //================================================================================
    testImplementation(kotlin("test"))

    //================================================================================
    // Kotlin
    //================================================================================
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:2.0.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    //================================================================================
    // bennyhuo
    //================================================================================
    // https://github.com/enbandari/ObjectPropertyDelegate
    implementation ("com.bennyhuo.kotlin:opd:1.0-rc-2")
    // https://github.com/enbandari/ReleasableVar
    implementation ("com.bennyhuo.kotlin:releasable-nonnull-vars:1.1.0")
    // https://github.com/enbandari/KotlinDeepCopy
    implementation ("com.bennyhuo.kotlin:deepcopy-reflect:1.0")
    //implementation ("com.bennyhuo.kotlin:deepcopy-annotations:1.1.0")
    //kapt ("com.bennyhuo.kotlin:deepcopy-compiler:1.1.0")
}


tasks.test {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_2_0)
    }
}

tasks.named<KotlinJvmCompile>("compileKotlin") {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

tasks.named<KotlinJvmCompile>("compileTestKotlin") {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

kotlin {
    jvmToolchain(17)
}