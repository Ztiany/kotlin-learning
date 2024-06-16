plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "Kotlin-Exploring"

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
