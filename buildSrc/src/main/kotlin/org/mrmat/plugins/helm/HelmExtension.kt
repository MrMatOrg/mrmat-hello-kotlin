package org.mrmat.plugins.helm

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property


interface HelmExtension {

    val configurationName: Property<String>
    val distributionName: Property<String>
    val srcPath: DirectoryProperty
    val buildPath: DirectoryProperty

    val namespace: Property<String>
    val releaseName: Property<String>

    val helmCommand: Property<String>
    val helmLintArgs: ListProperty<String>
    val helmDeployArgs: ListProperty<String>
    val helmRemoveArgs: ListProperty<String>
    val helmLintReport: RegularFileProperty
}
