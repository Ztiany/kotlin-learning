buildscript {
    extra["kotlin_plugin_id"] = "com.ztiany.kcp.demo.kotlin-ir-plugin"
}

plugins {
    kotlin("jvm") version "1.8.0" apply false
    kotlin("kapt") version "1.8.20" apply false
}


allprojects {

    group = "me.ztiany"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}