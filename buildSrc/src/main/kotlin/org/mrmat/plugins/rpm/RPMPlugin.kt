package org.mrmat.plugins.rpm

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class RPMPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply("base")
        project.plugins.apply(RPMBasePlugin::class.java)

        //
        // Opinionated task configuration and weaving

        project.tasks.named("rpmBuild") {
            dependsOn(
                project.tasks.named("rpmAssemble"),
                project.tasks.named("rpmBuildContainerScript"))
            mustRunAfter(
                project.tasks.named("rpmAssemble"),
                project.tasks.named("rpmBuildContainerScript"))
        }

        project.tasks.named("build") {
            dependsOn(project.tasks.named("rpmBuild"))
            mustRunAfter(project.tasks.named("rpmBuild"))
        }
    }
}
