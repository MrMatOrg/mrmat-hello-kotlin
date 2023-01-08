package org.mrmat.plugins.version

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class VersionExtension(project: Project) {

    val environmentVersion: Property<String>
    val localVersion: Property<String>

    init {
        environmentVersion = project.objects.property(String::class.java).convention("MRMAT_VERSION")
        localVersion = project.objects.property(String::class.java).convention("0.0.0-SNAPSHOT")
    }
}