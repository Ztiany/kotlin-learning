plugins {
    kotlin("jvm")
    kotlin("kapt")
    application
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