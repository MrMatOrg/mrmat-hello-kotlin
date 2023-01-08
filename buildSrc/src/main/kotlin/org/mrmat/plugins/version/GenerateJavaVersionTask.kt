package org.mrmat.plugins.version

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.lang.model.element.Modifier

abstract class GenerateJavaVersionTask: DefaultTask() {

    @get:Input
    abstract val version: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    init {
        version.convention("${project.version}")
        outputDir.convention(project.layout.buildDirectory.dir("generated/kotlinpoet/main/kotlin"))
    }

    @TaskAction
    fun execute() {
        val versionClass = TypeSpec
            .classBuilder("Version")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addField(String::class.java, "VERSION", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .build()
        val versionClassFile = JavaFile
            .builder(project.group.toString(), versionClass)
            .build()
        versionClassFile.writeTo(outputDir.asFile.get())
    }
}