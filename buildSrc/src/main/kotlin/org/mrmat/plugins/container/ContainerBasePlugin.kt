package org.mrmat.plugins.container

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register
import java.io.ByteArrayOutputStream

abstract class ContainerBasePlugin: Plugin<Project> {

    companion object {
        const val GROUP: String = "MrMat"
        const val EXT: String = "mrmatContainer"
    }

    override fun apply(project: Project): Unit {
        //
        // Establish configurability

        val containerExtension = project.extensions.create(EXT, ContainerExtension::class.java)

        //
        // Register the tasks

        project.tasks.register<Copy>("containerAssemble") {
            group = GROUP
            description = "Assemble the Container Image files"
            inputs.files(containerExtension.srcPath.asFileTree)
            outputs.dir(containerExtension.buildPath)
            from(containerExtension.srcPath)
            into(containerExtension.buildPath)
        }

        project.tasks.register<Exec>("containerBuild") {
            group = GROUP
            description = "Build the container image"
            inputs.files(containerExtension.buildPath.asFileTree)
            workingDir = containerExtension.buildPath.get().asFile
            executable = containerExtension.builderCommand.get()
            args(
                *containerExtension.builderCommandArgs.get().toTypedArray(),
                "--tag", "${containerExtension.imageName.get()}:${containerExtension.imageVersion.get()}",
                containerExtension.buildPath.get())
        }
    }
}
