package me.ztiany.kcp.plugin.gradle.demo

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption


/**
 * The Gradle plugin part of a Kotlin compiler plugin project is responsible for defining a few things:
 *
 * 1. Artifact coordinates of the Kotlin compiler plugin,
 * 2. ID string of the Kotlin compiler plugin,
 * 3. Translation of Gradle configuration to command line options.
 */
class DemoGradlePlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project): Unit = with(target) {
        extensions.create("template", DemoGradleExtension::class.java)
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return true
    }

    override fun getCompilerPluginId(): String = DEMO_KOTLIN_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = DEMO_KOTLIN_PLUGIN_GROUP, artifactId = DEMO_KOTLIN_PLUGIN_NAME, version = DEMO_KOTLIN_PLUGIN_VERSION
    )

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        val extension = project.extensions.getByType(DemoGradleExtension::class.java)
        return project.provider {
            listOf(
                SubpluginOption(key = "string", value = extension.stringProperty.get()),
                SubpluginOption(key = "file", value = extension.fileProperty.get().asFile.path),
            )
        }
    }

}