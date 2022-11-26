# MrMat :: Hello Kotlin

Kotlin experiments

## Versioning

If the 'MRMAT_VERSION' environment variable is set then Gradle will pick this up and set its build-time version from
its contents, otherwise it will fall back to '0.0.0-SNAPSHOT'.

The 'generateVersion' Gradle task will then generate a Kotlin class to relay that version towards the runtime.

