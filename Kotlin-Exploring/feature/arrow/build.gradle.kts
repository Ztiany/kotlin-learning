// refer to <https://kotlinlang.org/docs/gradle-compiler-options.html#target-the-jvm> for more details.
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.0.0"
}

dependencies {
    //================================================================================
    // test
    //================================================================================
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.1")

    //================================================================================
    // Kotlin
    //================================================================================
    // core
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:2.0.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx2:1.9.0-RC")
    // tools
    // https://github.com/Kotlin/kotlinx.serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.0")
    // https://github.com/Kotlin/kotlinx.atomicfu
    implementation("org.jetbrains.kotlinx:atomicfu:0.24.0")

    //================================================================================
    // network
    //================================================================================
    // retrofit with coroutine
    // https://github.com/square/retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:retrofit-mock:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.11.0")
    // https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    // okhttp
    // https://github.com/square/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // json
    implementation("com.google.code.gson:gson:2.10.1")

    //================================================================================
    // bennyhuo
    //================================================================================
    //https://github.com/enbandari/PortableAndroidHandler
    implementation("com.bennyhuo:portable-android-handler:1.0")

    //================================================================================
    // functional: arrow, mapstruct
    //================================================================================
    // arrow
    implementation("io.arrow-kt:arrow-core:2.0.0-alpha.3")
    //implementation ("io.arrow-kt:arrow-fx-coroutines:2.0.0-alpha.3")
    //implementation ("io.arrow-kt:arrow-optics:2.0.0-alpha.3")
    //ksp ("io.arrow-kt:arrow-optics-ksp-plugin:2.0.0-alpha.3")
    //implementation ("io.arrow-kt:arrow-syntax:2.0.0-alpha.3")
    //kapt ("io.arrow-kt:arrow-meta:2.0.0-alpha.3"
    // https://plugins.jetbrains.com/plugin/10036-mapstruct-support
    // https://github.com/mapstruct/mapstruct
    // https://mapstruct.org/news/2017-09-19-announcing-mapstruct-idea/
    //implementation ("org.mapstruct:mapstruct:1.4.2.Final")
    //kapt ("org.mapstruct:mapstruct-processor:1.4.2.Final")
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