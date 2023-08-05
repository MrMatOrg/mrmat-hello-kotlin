package org.mrmat.plugins.rpm

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface RPMExtension {

    val srcPath: DirectoryProperty
    val buildPath: DirectoryProperty

    val rpmSpec: Property<String>
    val rpmFile: RegularFileProperty

    val containerCommand: Property<String>
    val containerCommandArgs: ListProperty<String>
    val containerCommandScript: RegularFileProperty
    val container: Property<String>
}
