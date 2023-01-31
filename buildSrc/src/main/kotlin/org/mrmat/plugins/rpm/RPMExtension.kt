package org.mrmat.plugins.rpm

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

open class RPMExtension(project: Project) {

    companion object {
        const val DEFAULT_SRC_PATH = "src/main/rpm"
        const val DEFAULT_BUILD_PATH = "rpm"
        const val DEFAULT_CONTAINER_COMMAND = "docker"
        const val DEFAULT_CONTAINER_COMMAND_SCRIPT = "$DEFAULT_BUILD_PATH/build.sh"
        const val DEFAULT_CONTAINER = "redhat/ubi8:latest"
    }

    val srcPath: DirectoryProperty
    val buildPath: DirectoryProperty

    val rpmFile: RegularFileProperty

    val containerCommand: Property<String>
    val containerCommandArgs: ListProperty<String>
    val containerCommandScript: RegularFileProperty
    val container: Property<String>

    init {
        srcPath = project.objects.directoryProperty().convention(
            project.layout.projectDirectory.dir(DEFAULT_SRC_PATH)
        )
        buildPath = project.objects.directoryProperty().convention(
            project.layout.buildDirectory.dir(DEFAULT_BUILD_PATH)
        )

        rpmFile = project.objects.fileProperty().convention(
            project.layout.buildDirectory.file("$DEFAULT_BUILD_PATH/${project.name}.rpm")
        )

        containerCommand = project.objects.property(String::class.java).convention(DEFAULT_CONTAINER_COMMAND)
        containerCommandArgs = project.objects.listProperty(String::class.java).convention(
            listOf("run")
        )
        containerCommandScript = project.objects.fileProperty().convention(
            project.layout.buildDirectory.file(DEFAULT_CONTAINER_COMMAND_SCRIPT))
        container = project.objects.property(String::class.java).convention(DEFAULT_CONTAINER)

    }
}
