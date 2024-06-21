// refer to <https://kotlinlang.org/docs/gradle-compiler-options.html#target-the-jvm> for more details.
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.plugin.jetbrains.kotlin.jvm)
}

dependencies {
    testImplementation(libs.jetbrains.kotlin.test)
    testImplementation(libs.kotest.runner.junit5.jvm)

    implementation(libs.jetbrains.kotlin.stdlib)
    implementation(libs.jetbrains.kotlin.reflect)
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.coroutines.rx2)
    implementation(libs.jetbrains.kotlinx.serialization.core)
    implementation(libs.jetbrains.kotlinx.atomicfu)

    implementation(libs.squareup.retrofit2.core)
    implementation(libs.squareup.retrofit2.mock)
    implementation(libs.squareup.retrofit2.converter.gson)
    implementation(libs.squareup.retrofit2.adapter.rxjava2)
    implementation(libs.jakewharton.retrofit2.adapter.coroutine)
    implementation(libs.squareup.okhttp3.core)
    implementation(libs.squareup.okhttp3.mockwebserver)
    implementation(libs.squareup.okhttp3.interceptor.log)
    implementation(libs.google.gson)

    implementation(libs.arrow.core)
    implementation(libs.arrow.syntax)

    implementation(libs.bennyhuo.android.handler)
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