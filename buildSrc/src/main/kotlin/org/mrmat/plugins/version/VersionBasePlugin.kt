package org.mrmat.plugins.version

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

abstract class VersionBasePlugin: Plugin<Project> {

    companion object {
        const val GROUP: String = "MrMat"
        const val EXT: String = "mrmatVersion"
    }

    override fun apply(project: Project) {

        //
        // Establish configurability

        val versionExtension = project.extensions.create(EXT, VersionExtension::class.java)
        versionExtension.environmentVersion.convention("MRMAT_VERSION")
        versionExtension.localVersion.convention("0.0.0-SNAPSHOT")

        //
        // Register the tasks

        project.tasks.register<GenerateKotlinVersionTask>("generateKotlinVersion") {
            group = GROUP
            description = "Generate Kotlin sources holding the current version"
        }

        project.tasks.register<GenerateJavaVersionTask>("generateJavaVersion") {
            group = GROUP
            description = "Generate Java sources holding the current version"
        }
    }
}
