// refer to <https://kotlinlang.org/docs/gradle-compiler-options.html#target-the-jvm> for more details.
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.0.0"

    // https://kotlinlang.org/docs/no-arg-plugin.html
    kotlin("plugin.noarg") version "2.0.0"
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
    // json
    //================================================================================
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
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

noArg {
    annotation("me.ztiany.kotlin.noarg.NoArg")
    invokeInitializers = true
}