package org.mrmat.plugins.version

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class VersionPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply(VersionBasePlugin::class.java)
        val versionExtension = project.extensions.getByType<VersionExtension>(VersionExtension::class.java)

        project.version =
            System.getenv(versionExtension.environmentVersion.get()) ?: versionExtension.localVersion.get()
    }

}