plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
    id("maven-publish")
}

description = "A Gradle plugin that is used for including kotlin compiler plugins in a project."

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin-api"))
}

gradlePlugin {
    plugins {
        create("kcpDemoPlugin") {
            id = "me.ztiany.kcp.plugin.demo"
            implementationClass = "me.ztiany.kcp.plugin.gradle.demo.DemoGradlePlugin"
        }
        create("kcpDebugLogPlugin") {
            id = "me.ztiany.kcp.plugin.debuglog"
            implementationClass = "me.ztiany.kcp.plugin.gradle.debug.DebugLogGradlePlugin"
        }
    }
}