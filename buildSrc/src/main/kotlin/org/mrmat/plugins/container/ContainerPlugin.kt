package org.mrmat.plugins.container

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named


abstract class ContainerPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply(ContainerBasePlugin::class.java)
        val containerExtension = project.extensions.getByType<ContainerExtension>()

        //
        // Opinionated task configuration and weaving

        project.tasks.named<Exec>("containerRunLocal") {
            dependsOn(project.tasks.named<Exec>("containerBuildLocal"))
            mustRunAfter(project.tasks.named<Exec>("containerBuildLocal"))
        }

        project.tasks.named("containerBuildCI") {
            dependsOn(project.tasks.named<Copy>("containerAssemble"))
            mustRunAfter(project.tasks.named<Copy>("containerAssemble"))
        }

        project.tasks.named<Exec>("containerBuildLocal") {
            dependsOn(project.tasks.named<Copy>("containerAssemble"))
            mustRunAfter(project.tasks.named<Copy>("containerAssemble"))
        }

        project.tasks.named("build") {
            dependsOn(
                project.tasks.named<Copy>("containerAssemble"),
                if(containerExtension.ci.get())
                    project.tasks.named("containerBuildCI")
                else
                    project.tasks.named("containerBuildLocal"))
        }
    }
}
