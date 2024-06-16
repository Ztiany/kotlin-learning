// refer to <https://kotlinlang.org/docs/gradle-compiler-options.html#target-the-jvm> for more details.
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.0.0"
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
    // Kotlin/JVM: Metadata and Reflection
    //================================================================================
    // https://github.com/JetBrains/kotlin/tree/master/libraries/kotlinx-metadata/jvm
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.9.0")
    // https://square.github.io/kotlinpoet/interop-kotlinx-metadata/
    implementation("com.squareup:kotlinpoet-metadata:1.17.0")
    // https://github.com/bennyhuo/kotlinp
    implementation("com.bennyhuo.kotlin:kotlinp:1.8.10")
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