# MrMat :: Hello Kotlin

Kotline Experiments

## Auto-Versioning

If the 'MRMAT_VERSION' environment variable is set by CI or manually then Gradle will pick this up and assign its contents to the artifact version it builds. Whilst developing locally and interactively, with 'MRMAT_VERSION' not set it will fall back to a default of '0.0.0-SNAPSHOT'.

Whichever version is used at build time is relayed to the runtime via a class that is constructed by the 'generateVersion' Gradle task. The class exposes the 'VERSION' using a companion object.

