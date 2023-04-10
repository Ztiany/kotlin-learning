package me.ztiany.kapt.processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import me.ztiany.kapt.annotations.Code
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

private const val SUFFIX = "Autogenerate"

@SupportedAnnotationTypes("me.ztiany.kapt.annotations.Code")
@SupportedOptions("module_name")
class CodeAnnotationProcessor : AbstractProcessor() {

    private lateinit var messager: Messager

    private lateinit var filer: Filer

    private var moduleName = ""

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        filer = processingEnv.filer
        println("CodeAnnotationProcessor.init is called.")
        moduleName = processingEnv.options["module-name"] ?: ""
        messager.printMessage(Diagnostic.Kind.NOTE, "module_name = $moduleName")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        println("CodeAnnotationProcessor.process is called.")
        println("handling annotations: $annotations.")

        roundEnv.getElementsAnnotatedWith(Code::class.java).forEach {
            it.getAnnotation(Code::class.java)?.let { code ->
                generateCode(it, code)
            }
        }

        return true
    }

    private fun generateCode(element: Element, code: Code) {
        val typeElement = element.enclosingElement as TypeElement
        val packageName = typeElement.qualifiedName.toString()
        println("typeElement.packageName: $typeElement")

        val clazz = ClassName("", "${typeElement.simpleName}$SUFFIX")

        FileSpec.builder(
            packageName.substring(0, packageName.lastIndexOf(".")),
            "${typeElement.simpleName}$SUFFIX"
        ).addType(
            TypeSpec.Companion.classBuilder(clazz)
                .addFunction(
                    FunSpec.builder("message")
                        .addStatement("println(%P)", "author = ${code.author}, data = ${code.date}")
                        .build()
                )
                .build()
        )
            .build()
            .writeTo(filer)
    }

}