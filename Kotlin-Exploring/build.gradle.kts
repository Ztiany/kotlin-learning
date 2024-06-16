subprojects {
    repositories {
        repositories {
            google()
            mavenCentral()
            maven { url = uri("https://maven.aliyun.com/repository/public") }
            maven { url = uri("https://maven.aliyun.com/repository/central") }
            maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
            maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
            gradlePluginPortal()
        }
    }
}
