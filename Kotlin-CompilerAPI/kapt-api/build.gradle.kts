plugins {
    kotlin("jvm")
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
