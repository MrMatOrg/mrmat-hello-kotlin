package org.mrmat.plugins.ansible

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.StopExecutionException
import org.gradle.kotlin.dsl.register
import java.io.ByteArrayOutputStream

abstract class AnsibleBasePlugin: Plugin<Project> {

    companion object {
        const val GROUP: String = "MrMat"
        const val EXT: String = "mrmatAnsible"
    }

    override fun apply(project: Project) {

        //
        // Establish configurability

        val ansibleExtension = project.extensions.create(EXT, AnsibleExtension::class.java)
        ansibleExtension.configurationName.convention("ansiblePlaybook")
        ansibleExtension.distributionName.convention("ansiblePlaybook")
        ansibleExtension.srcPath.convention(project.layout.projectDirectory.dir("src/main/ansible"))
        ansibleExtension.buildPath.convention((project.layout.buildDirectory.dir("ansible")))
        ansibleExtension.ansiblePlaybookCommand.convention("ansible-playbook")
        //        ansiblePlaybookArgs = project.objects.listProperty(String::class.java)
        ansibleExtension.ansiblePlaybookInventory.convention(ansibleExtension.buildPath.dir("inventories/dev"))
        ansibleExtension.ansiblePlaybook.convention(ansibleExtension.buildPath.file("deploy.yaml"))
        ansibleExtension.ansibleLintCommand.convention("ansible-lint")
        ansibleExtension.ansibleLintReport.convention(project.layout.buildDirectory.file("reports/ansible-lint.txt"))
        ansibleExtension.ansibleAppVarsFile.convention(ansibleExtension.buildPath.file("group_vars/all/app.yaml"))

        //
        // Register the tasks

        project.tasks.register<AnsibleVersionTask>("ansibleVersion") {
            group = GROUP
            description = "Set the project version on the Ansible Playbook"
            inputs.property("project.version", "${project.version}")
            outputs.file(ansibleExtension.ansibleAppVarsFile)
        }

        project.tasks.register<Copy>("ansibleAssemble") {
            group = GROUP
            description = "Assemble the Ansible Playbook"
            inputs.files(ansibleExtension.srcPath.asFileTree)
            outputs.files(ansibleExtension.buildPath.files())
            from(ansibleExtension.srcPath) {
                exclude(
                    ansibleExtension.ansibleAppVarsFile.get().asFile
                        .relativeTo(ansibleExtension.buildPath.get().asFile).toString()
                )
            }
            into(ansibleExtension.buildPath)
        }

        project.tasks.register<Exec>("ansibleLint") {
            group = GROUP
            description = "Lint the Ansible Playbook"
            inputs.files(ansibleExtension.buildPath.asFileTree)
            outputs.file(ansibleExtension.ansibleLintReport)
            workingDir = ansibleExtension.buildPath.get().asFile
            environment.put("PATH", "${project.file(ansibleExtension.ansibleLintCommand).absoluteFile.parent}:{$environment.get(\"PATH\")}")
            standardOutput = ByteArrayOutputStream()
            errorOutput = ByteArrayOutputStream()
            executable = ansibleExtension.ansibleLintCommand.get()
            args(*ansibleExtension.ansibleLintArgs.get().toTypedArray(), ansibleExtension.buildPath.get())
            setIgnoreExitValue(true)

            doLast {
                project.mkdir(ansibleExtension.ansibleLintReport.get().asFile.parent)
                ansibleExtension.ansibleLintReport.get().asFile.writeText("""
                    |Ansible Lint:
                    |${standardOutput}
                    |${errorOutput}
                    """.trimMargin())
                if (executionResult.get().exitValue != 0) {
                    logger.error("Ansible Lint:\n${standardOutput}\n${errorOutput}")
                    throw StopExecutionException("Ansible Playbook does not lint")
                } else {
                    logger.info("Ansible Lint:\n${standardOutput}\n${errorOutput}")
                }
            }
        }

        project.tasks.register<Exec>("ansibleDeploy") {
            group = GROUP
            description = "Deploy the Ansible Playbook"
            environment.put("PATH", "${project.file(ansibleExtension.ansiblePlaybookCommand).absoluteFile.parent}:{$environment.get(\"PATH\")}")
            workingDir = ansibleExtension.buildPath.get().asFile
            executable = ansibleExtension.ansiblePlaybookCommand.get()
            args(
                *ansibleExtension.ansiblePlaybookArgs.get().toTypedArray(),
                "--inventory", ansibleExtension.ansiblePlaybookInventory.get(),
                ansibleExtension.ansiblePlaybook.get())
        }
    }
}
