package org.mrmat.plugins.ansible

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface AnsibleExtension {
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
}
