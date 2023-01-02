package org.mrmat.hello.kotlin.test

import org.mrmat.hello.kotlin.app.versioning.Version
import kotlin.test.Test

class VersionTest {

    @Test
    fun checkVersion() {
        val envVersion = System.getenv("MRMAT_VERSION") ?: "0.0.0-SNAPSHOT"
        assert(envVersion == Version.VERSION)
    }
}
