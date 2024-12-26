plugins {
    kotlin("jvm") version "1.8.20" apply false
    kotlin("kapt") version "1.8.20" apply false
    id("me.ztiany.kcp.plugin.demo") version "1.0.1" apply false
    id("me.ztiany.kcp.plugin.debuglog") version "1.0.1" apply false
    id("com.google.devtools.ksp") version "1.8.20-1.0.10" apply false
}

allprojects {
    group = "me.ztiany"
    version = "1.0.1"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}