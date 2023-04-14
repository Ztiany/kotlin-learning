rootProject.name = "Kotlin-CompilerAPI"

include("kapt-api")
include("kapt-main")
include("kcp-plugin-gradle")
include("kcp-plugin-kotlin-demo")
include("kcp-plugin-kotlin-debuglog")
include("kcp-main")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
include("kcp-annotation-debuglog")
