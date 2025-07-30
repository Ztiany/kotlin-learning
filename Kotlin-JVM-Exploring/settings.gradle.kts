pluginManagement {
    repositories {
        mavenLocal()
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":common:extension")

include(":feature:essential")
include(":feature:coroutine")
include(":feature:ktnew")
include(":feature:noarg")
include(":feature:arrow")
include(":feature:effective")
include(":feature:rxkotlin")
include(":feature:imooc")
include(":feature:ktmate")

rootProject.name = "Kotlin-JVM-Exploring"
