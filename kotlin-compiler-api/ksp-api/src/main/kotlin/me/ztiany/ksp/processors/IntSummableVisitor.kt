package me.ztiany.ksp.processors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec

class IntSummableVisitor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
    private val intType: KSType
) : KSVisitorVoid() {

    private lateinit var className: String
    private lateinit var packageName: String

    private val summableList: MutableList<String> = mutableListOf()

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        //log the function is being called
        logger.info("visitClassDeclaration: ${classDeclaration.qualifiedName?.asString()}")

        val qualifiedName = classDeclaration.qualifiedName?.asString()

        //1. 合法性检查
        if (classDeclaration.classKind != ClassKind.CLASS) {
            logger.error("@IntSummable can only target on class $qualifiedName", classDeclaration)
            return
        }

        if (qualifiedName == null) {
            logger.error("@IntSummable must target classes with qualified names", classDeclaration)
            return
        }

        //2. 解析Class信息
        className = qualifiedName
        packageName = classDeclaration.packageName.asString()

        classDeclaration.getAllProperties().forEach { it.accept(this, Unit) }

        if (summableList.size <= 1) {
            logger.warn("The class $qualifiedName must has more than 2 int filed", classDeclaration)
            return
        }

        //3. 生成代码
        val fileSpec = FileSpec.builder(
            packageName = packageName,
            fileName = classDeclaration.simpleName.asString()
        ).apply {
            addFunction(
                FunSpec.builder("sumInts")
                    .receiver(ClassName.bestGuess(className))
                    .returns(Int::class)
                    .addStatement("val sum = ${summableList.joinToString(" + ")}")
                    .addStatement("return sum")
                    .build()
            )
        }.build()

        codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = false),
            packageName = packageName,
            fileName = classDeclaration.simpleName.asString()
        ).use { outputStream ->
            outputStream.writer()
                .use {
                    fileSpec.writeTo(it)
                }
        }

        //log the function is finished
        logger.info("visitClassDeclaration: ${classDeclaration.qualifiedName?.asString()} finished")
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
        if (property.type.resolve().isAssignableFrom(intType)) {
            val name = property.simpleName.asString()
            summableList.add(name)
        }
    }

}