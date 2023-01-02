package org.mrmat

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.work.Incremental
import org.gradle.work.InputChanges


abstract class GenerateVersionTask : DefaultTask() {

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
        val versionProperty = PropertySpec
            .builder("VERSION", String::class, com.squareup.kotlinpoet.KModifier.CONST)
            .initializer("%S", version.get())
            .build()
        val versionCompanionObject = TypeSpec
            .companionObjectBuilder()
            .addProperty(versionProperty)
            .build()
        val versionClass = TypeSpec
            .classBuilder("Version")
            .addType(versionCompanionObject)
            .build()
        val versionClassFile = FileSpec
            .builder(project.group.toString(), "Version")
            .addType(versionClass)
            .indent("    ")
            .build()
        versionClassFile.writeTo(outputDir.asFile.get())
    }
}