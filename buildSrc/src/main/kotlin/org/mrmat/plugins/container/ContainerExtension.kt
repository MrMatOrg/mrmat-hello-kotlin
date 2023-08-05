package org.mrmat.plugins.container

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface ContainerExtension {

    val srcPath: DirectoryProperty
    val buildPath: DirectoryProperty
    val dockerFile: RegularFileProperty

    val imageName: Property<String>
    val imageVersion: Property<String>
    val imageVersionLabel: Property<String>
    val imageAppLabel: Property<String>

    val builderCommand: Property<String>
    val builderCommandArgs: ListProperty<String>
    val runCommandArgs: ListProperty<String>

    val ci: Property<Boolean>
    val ciBuildFile: RegularFileProperty
}
