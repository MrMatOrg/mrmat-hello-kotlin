package org.mrmat.plugins.helm

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property


open class HelmExtension(project: Project) {

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


    init {
        configurationName = project.objects.property(String::class.java).convention("helmChart")
        distributionName = project.objects.property(String::class.java).convention("helmChart")
        srcPath = project.objects.directoryProperty().convention(
            project.layout.projectDirectory.dir("src/main/helm")
        )
        buildPath = project.objects.directoryProperty().convention(
            project.layout.buildDirectory.dir("helm")
        )

        namespace = project.objects.property(String::class.java).convention(project.rootProject.name)
        releaseName = project.objects.property(String::class.java).convention(project.rootProject.name)

        helmCommand = project.objects.property(String::class.java).convention("helm")
        helmLintArgs = project.objects.listProperty(String::class.java).convention(
            listOf("lint")
        )
        helmDeployArgs = project.objects.listProperty(String::class.java).convention(
            listOf("upgrade", "--install", "--create-namespace", "--namespace", namespace.get(), )
        )
        helmRemoveArgs = project.objects.listProperty(String::class.java).convention(
            listOf("uninstall", "--namespace", namespace.get())
        )
        helmLintReport = project.objects.fileProperty().convention(
            project.layout.buildDirectory.file("reports/helm-lint.txt")
        )

    }
}
