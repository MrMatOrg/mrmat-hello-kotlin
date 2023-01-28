package org.mrmat.plugins.container

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register

abstract class ContainerBasePlugin: Plugin<Project> {

    companion object {
        const val GROUP: String = "MrMat"
        const val EXT: String = "mrmatContainer"
    }

    override fun apply(project: Project) {
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

        project.tasks.register<Exec>("containerRunLocal") {
            group = GROUP
            description = "Run the container locally"
            workingDir = containerExtension.buildPath.get().asFile
            executable = containerExtension.builderCommand.get()
            args(
                *containerExtension.runCommandArgs.get().toTypedArray(),
                "${containerExtension.imageName.get()}:${containerExtension.imageVersion.get()}"
            )
        }

        project.tasks.register<Exec>("containerBuildLocal") {
            group = GROUP
            description = "Build the container image when container build utilities are present"
            inputs.files(containerExtension.buildPath.asFileTree)
            workingDir = containerExtension.buildPath.get().asFile
            executable = containerExtension.builderCommand.get()
            args(
                *containerExtension.builderCommandArgs.get().toTypedArray(),
                "--tag", "${containerExtension.imageName.get()}:${containerExtension.imageVersion.get()}",
                "--label", "${containerExtension.imageVersionLabel.get()}=${containerExtension.imageVersion.get()}",
                "--label", "${containerExtension.imageAppLabel.get()}=${project.rootProject.name}",
                containerExtension.buildPath.get())

            doFirst {
                logger.warn("Your Gradle build is orchestrating the container image build")
            }
        }

        project.tasks.register("containerBuildCI") {
            group = GROUP
            description = "Generate a container image build shell script when on CI"
            inputs.files(containerExtension.buildPath.asFileTree)
            outputs.file(containerExtension.ciBuildFile)

            doLast {
                logger.warn(
                    "Your CI is orchestrating the container image build. Execute " +
                            "${containerExtension.ciBuildFile.get()} in a job where container image building tools " +
                            "are available")
                containerExtension.ciBuildFile.get().asFile.writeText("""
                |#!/bin/bash
                |${containerExtension.builderCommand.get()} \
                |   ${containerExtension.builderCommandArgs.get().joinToString(" ")} \
                |   -f ${containerExtension.dockerFile.get()} \
                |   --tag ${containerExtension.imageName.get()}:${containerExtension.imageVersion.get()} \
                |   --label ${containerExtension.imageVersionLabel.get()}=${containerExtension.imageVersion.get()} \
                |   --label ${containerExtension.imageAppLabel.get()}=${project.rootProject.name} \
                |   ${containerExtension.buildPath.get()}
                """.trimMargin())
                containerExtension.ciBuildFile.get().asFile.setExecutable(true)
            }
        }
    }
}
