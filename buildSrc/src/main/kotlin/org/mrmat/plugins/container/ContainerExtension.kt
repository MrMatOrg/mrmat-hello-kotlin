package org.mrmat.plugins.container

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

open class ContainerExtension(project: Project) {

    val srcPath: DirectoryProperty
    val buildPath: DirectoryProperty
    val dockerFile: RegularFileProperty

    val imageName: Property<String>
    val imageVersion: Property<String>

    val builderCommand: Property<String>
    val builderCommandArgs: ListProperty<String>

    init {
        srcPath = project.objects.directoryProperty().convention(
            project.layout.projectDirectory.dir("src/main/container")
        )
        buildPath = project.objects.directoryProperty().convention(
            project.layout.buildDirectory.dir("container")
        )

        dockerFile = project.objects.fileProperty().convention(
            buildPath.file("Dockerfile")
        )

        imageName = project.objects.property(String::class.java).convention(project.rootProject.name)
        imageVersion = project.objects.property(String::class.java).convention("${project.version}")

        builderCommand = project.objects.property(String::class.java).convention("docker")
        builderCommandArgs = project.objects.listProperty(String::class.java).convention(
            listOf("build")
        )
    }
}
