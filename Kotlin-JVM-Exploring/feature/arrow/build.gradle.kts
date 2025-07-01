// refer to <https://kotlinlang.org/docs/gradle-compiler-options.html#target-the-jvm> for more details.
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.plugin.jetbrains.kotlin.jvm)
    alias(libs.plugins.plugin.google.kotlin.ksp2)
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
    implementation(libs.arrow.fx.coroutines)
    implementation(libs.arrow.optics.core)
    //ksp(libs.arrow.optics.ksp) // not support ksp 2.0

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
        freeCompilerArgs.addAll("-Xcontext-receivers", "-Xcontext-parameters")
    }
}

tasks.named<KotlinJvmCompile>("compileTestKotlin") {
    compilerOptions {
        freeCompilerArgs.addAll("-Xcontext-receivers", "-Xcontext-parameters")
    }
}

kotlin {
    jvmToolchain(17)
}