package org.mrmat.plugins.ansible

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

open class AnsibleExtension(project: Project) {

    val configurationName: Property<String>
    val distributionName: Property<String>
    val srcPath: DirectoryProperty
    val buildPath: DirectoryProperty

    val ansiblePlaybookCommand: Property<String>
    val ansiblePlaybookArgs: ListProperty<String>
    val ansiblePlaybookInventory: DirectoryProperty
    val ansiblePlaybook: RegularFileProperty
    val ansibleLintCommand: Property<String>
    val ansibleLintArgs: ListProperty<String>
    val ansibleLintReport: RegularFileProperty

    val ansibleAppVarsFile: RegularFileProperty

    init {
        configurationName = project.objects.property(String::class.java).convention("ansiblePlaybook")
        distributionName = project.objects.property(String::class.java).convention("ansiblePlaybook")
        srcPath = project.objects.directoryProperty().convention(
            project.layout.projectDirectory.dir("src/main/ansible")
        )
        buildPath = project.objects.directoryProperty().convention(
            project.layout.buildDirectory.dir("ansible")
        )

        ansiblePlaybookCommand = project.objects.property(String::class.java).convention("ansible-playbook")
        ansiblePlaybookArgs = project.objects.listProperty(String::class.java)
        ansiblePlaybookInventory = project.objects.directoryProperty().convention(
            buildPath.get().dir("inventories/dev")
        )
        ansiblePlaybook = project.objects.fileProperty().convention(
            buildPath.get().file("deploy.yml")
        )
        ansibleLintCommand = project.objects.property(String::class.java).convention("ansible-lint")
        ansibleLintArgs = project.objects.listProperty(String::class.java).convention(listOf("--nocolor"))
        ansibleLintReport = project.objects.fileProperty().convention(
            project.layout.buildDirectory.file("reports/ansible-lint.txt")
        )

        ansibleAppVarsFile = project.objects.fileProperty().convention(
            buildPath.get().file("group_vars/all/app.yml")
        )
    }
}
