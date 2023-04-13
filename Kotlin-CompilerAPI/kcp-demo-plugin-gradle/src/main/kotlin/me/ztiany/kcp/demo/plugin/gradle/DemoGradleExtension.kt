package me.ztiany.kcp.demo.plugin.gradle

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

open class DemoGradleExtension(objects: ObjectFactory) {

    val stringProperty: Property<String> = objects.property(String::class.java)

    val fileProperty: RegularFileProperty = objects.fileProperty()

}