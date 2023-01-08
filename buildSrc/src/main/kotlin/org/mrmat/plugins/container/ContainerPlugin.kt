package org.mrmat.plugins.container

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.named


abstract class ContainerPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply(ContainerBasePlugin::class.java)

        val containerExtension = project.extensions.getByType<ContainerExtension>(ContainerExtension::class.java)

        //
        // Opinionated task configuration and weaving

        project.tasks.named("build") {
            dependsOn(
                project.tasks.named<Copy>("containerAssemble"),
                if(containerExtension.ci.get())
                    project.tasks.named<Exec>("containerBuildLocal")
                else
                    project.tasks.named("containerBuildCI"))
        }
    }
}
