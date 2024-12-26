package me.ztiany.kcp.plugin.gradle.debug

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class DebugLogGradlePlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project): Unit = with(target) {
        extensions.create("debuglog", DebugLogGradleExtension::class.java)
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return true
    }

    override fun getCompilerPluginId(): String = DEBUG_LOG_KOTLIN_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = DEBUG_LOG_KOTLIN_PLUGIN_GROUP,
        artifactId = DEBUG_LOG_KOTLIN_PLUGIN_NAME,
        version = DEBUG_LOG_KOTLIN_PLUGIN_VERSION
    )

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        kotlinCompilation.dependencies {
            compileOnly("${ANNOTATION_LIBRARY_GROUP}:${ANNOTATION_LIBRARY_NAME}:${ANNOTATION_LIBRARY_VERSION}")
        }

        val project = kotlinCompilation.target.project
        val extension = project.extensions.getByType(DebugLogGradleExtension::class.java)

        return project.provider {
            listOf(
                SubpluginOption(key = "enabled", value = extension.enabled.toString()),
            )
        }
    }

}