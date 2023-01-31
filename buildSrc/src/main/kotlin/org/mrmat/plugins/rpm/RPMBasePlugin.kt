package org.mrmat.plugins.rpm

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register

class RPMBasePlugin: Plugin<Project> {

    companion object {
        const val GROUP: String = "MrMat"
        const val EXT: String = "mrmatRPM"
    }

    override fun apply(project: Project) {
        val rpmExtension = project.extensions.create(EXT, RPMExtension::class.java)

        project.tasks.register<Copy>("rpmAssemble") {
            group = GROUP
            description = "Assemble the RPM files"
            inputs.files(rpmExtension.srcPath.asFileTree)
            outputs.dir(rpmExtension.buildPath)
            from(rpmExtension.srcPath)
            into(rpmExtension.buildPath)
        }

        project.tasks.register("rpmBuildContainerScript") {
            group = GROUP
            description = "Create a script to be executed in the RPM build container"
            inputs.property("project.version", project.version.toString())
            outputs.file(rpmExtension.containerCommandScript)

            doLast {
                rpmExtension.containerCommandScript.get().asFile.writeText("""
                    |#!/bin/bash
                    |dnf install -y rpm-build
                    |cd /rpm
                    |rpmbuild -ba hello-world.spec
                    |cp /root/rpmbuild/RPMS/aarch64/hello-world-1-1.aarch64.rpm /rpm/foo.rpm
                """.trimMargin())
                rpmExtension.containerCommandScript.get().asFile.setExecutable(true)
            }
        }

        project.tasks.register<Exec>("rpmBuildContainer") {
            group = GROUP
            description = "Build the RPM in a container image"
            inputs.property("project.version", project.version.toString())
            inputs.files(rpmExtension.buildPath.files())
            //outputs.files(rpmExtension.rpmFile.get())
            workingDir = rpmExtension.buildPath.get().asFile
            executable = rpmExtension.containerCommand.get()
            args(
                *rpmExtension.containerCommandArgs.get().toTypedArray(),
                "-v", "${rpmExtension.buildPath.get()}:/rpm",
                rpmExtension.container.get(),
                "/rpm/build.sh"
            )
        }
    }
}
