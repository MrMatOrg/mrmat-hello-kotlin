package org.mrmat.plugins.version

import org.gradle.api.provider.Property

interface VersionExtension {

    val environmentVersion: Property<String>
    val localVersion: Property<String>
}