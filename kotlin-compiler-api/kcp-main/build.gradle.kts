plugins {
    kotlin("jvm")
    id("me.ztiany.kcp.plugin.debuglog")
    application
}

dependencies {
    // not necessary, cause the plugin will add it.
    //implementation("me.ztiany:kcp-annotation-debuglog:1.0.1")
    testImplementation(kotlin("test"))
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