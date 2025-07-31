plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
    application
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":ksp-api"))
    ksp(project(":ksp-api"))
}

ksp {
    arg("module_name", "ksp_main")
}


tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}

kotlin {
    jvmToolchain(11)
}
