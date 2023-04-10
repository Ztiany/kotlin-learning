plugins {
    kotlin("jvm") version "1.8.0"
}

group = "me.ztiany"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.squareup:kotlinpoet-ksp:1.13.0")
    implementation("com.squareup:kotlinpoet-metadata:1.13.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}
