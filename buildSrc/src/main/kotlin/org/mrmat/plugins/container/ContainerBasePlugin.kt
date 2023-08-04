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
        containerExtension.srcPath.convention(project.layout.projectDirectory.dir("src/main/container"))
        containerExtension.buildPath.convention(project.layout.buildDirectory.dir("container"))
        containerExtension.dockerFile.convention(containerExtension.buildPath.file("Dockerfile"))
        containerExtension.imageName.convention(project.rootProject.name)
        containerExtension.imageVersion.convention("${project.version}")
        containerExtension.imageVersionLabel.convention("org.mrmat.app.version")
        containerExtension.imageAppLabel.convention("org.mrmat.app.name")
        containerExtension.builderCommand.convention("docker")
        containerExtension.builderCommandArgs.convention(listOf("build"))
        containerExtension.runCommandArgs.convention(listOf("run", "-iP", "--rm"))
        containerExtension.ci.convention(System.getenv("CI") != null)
        containerExtension.ciBuildFile.convention(containerExtension.buildPath.file("build.sh"))


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
            inputs.property("project.version", project.version.toString())
            inputs.files(containerExtension.buildPath.files())
            outputs.file(containerExtension.buildPath.file("image").get().asFile)
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
            doLast {
                logger.lifecycle("Build container image ${containerExtension.imageName.get()}:${containerExtension.imageVersion.get()}")
                containerExtension.buildPath.file("image").get().asFile.writeText("${containerExtension.imageName.get()}:${containerExtension.imageVersion.get()}")
            }
        }

        project.tasks.register("containerBuildCI") {
            group = GROUP
            description = "Generate a container image build shell script when on CI"
            inputs.files(containerExtension.buildPath.files())
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
