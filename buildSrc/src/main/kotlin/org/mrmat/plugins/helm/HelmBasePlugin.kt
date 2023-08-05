package org.mrmat.plugins.helm

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.StopExecutionException
import org.gradle.kotlin.dsl.register
import java.io.ByteArrayOutputStream

abstract class HelmBasePlugin: Plugin<Project> {

    companion object {
        const val GROUP: String = "MrMat"
        const val EXT: String = "mrmatHelm"
    }

    override fun apply(project: Project) {

        //
        // Establish configurability

        val helmExtension = project.extensions.create(EXT, HelmExtension::class.java)
        helmExtension.configurationName.convention("helmChart")
        helmExtension.distributionName.convention("helmChart")
        helmExtension.srcPath.convention(project.layout.projectDirectory.dir("src/main/helm"))
        helmExtension.buildPath.convention(project.layout.buildDirectory.dir("helm"))
        helmExtension.namespace.convention(project.rootProject.name)
        helmExtension.releaseName.convention(project.rootProject.name)
        helmExtension.helmCommand.convention("helm")
        helmExtension.helmLintArgs.convention(listOf("lint"))
        helmExtension.helmDeployArgs.convention(
            listOf("upgrade", "--install", "--create-namespace", "--namespace", helmExtension.namespace.get()))
        helmExtension.helmRemoveArgs.convention(
            listOf("uninstall", "--namespace", helmExtension.namespace.get())
        )
        helmExtension.helmLintReport.convention(project.layout.buildDirectory.file("reports/helm-lint.txt"))

        //
        // Register the tasks

        project.tasks.register<HelmVersionTask>("helmVersion") {
            group = GROUP
            description = "Set the project version on the Helm chart"
            inputs.property("project.version", "${project.version}")
            outputs.file(helmExtension.buildPath.file("Chart.yaml"))
        }

        project.tasks.register<Copy>("helmAssemble") {
            group = GROUP
            description = "Assemble the Helm chart"
            inputs.files(helmExtension.srcPath.asFileTree)
            outputs.files(helmExtension.buildPath.asFileTree)
            from(helmExtension.srcPath) {
                exclude("Chart.yaml")
            }
            into(helmExtension.buildPath.get())
        }

        project.tasks.register<Exec>("helmLint") {
            group = GROUP
            description = "Lint the Helm chart"
            inputs.files(helmExtension.buildPath.asFileTree)
            outputs.file(helmExtension.helmLintReport)
            workingDir = helmExtension.buildPath.get().asFile
            standardOutput = ByteArrayOutputStream()
            errorOutput = ByteArrayOutputStream()
            executable = helmExtension.helmCommand.get()
            args(*helmExtension.helmLintArgs.get().toTypedArray(), helmExtension.buildPath.get())
            setIgnoreExitValue(true)

            doLast {
                project.mkdir(helmExtension.helmLintReport.get().asFile.parent)
                helmExtension.helmLintReport.get().asFile.writeText("""
                    |Helm Lint:
                    |${standardOutput}
                    |${errorOutput}
                    """.trimMargin())
                if (executionResult.get().exitValue != 0) {
                    logger.error("Helm Lint:\n${standardOutput}\n${errorOutput}")
                    throw StopExecutionException("Helm chart does not lint")
                } else {
                    logger.info("Helm Lint:\n${standardOutput}\n${errorOutput}")
                }
            }
        }

        project.tasks.register<Exec>("helmDeployLocal") {
            group = GROUP
            description = "Deploy the Helm chart locally"
            workingDir = helmExtension.buildPath.get().asFile
            executable = helmExtension.helmCommand.get()
            args(
                *helmExtension.helmDeployArgs.get().toTypedArray(),
                helmExtension.releaseName.get(),
                ".")
        }

        project.tasks.register<Exec>("helmRemoveLocal") {
            group = GROUP
            description = "Remove a local deployment of the Helm chart"
            executable = helmExtension.helmCommand.get()
            args(
                *helmExtension.helmRemoveArgs.get().toTypedArray(),
                helmExtension.releaseName.get()
            )
        }
    }
}
