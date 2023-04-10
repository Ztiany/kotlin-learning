plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("kapt") version "1.8.20"
    application
}

group = "me.ztiany"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":kapt-api"))
    kapt(project(":kapt-api"))
}

kapt {
    arguments {
        arg("module_name", "main")
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}