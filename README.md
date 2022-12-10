# MrMat :: Hello Kotlin

Kotlin Experiments

[![Build](https://github.com/MrMatAP/mrmat-hello-kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/MrMatAP/mrmat-hello-kotlin/actions/workflows/build.yml)

## How to use this

This repository serves as a demonstration on how to get a Kotline project on its feet. There is currently nothing truly
functional demonstrated except for a reasonable CI process.

## How to build this

### Interactively

Just run `./gradlew build` locally. An interactive build will default to '0.0.0-SNAPSHOT' for its version, which is
a relevant marker showcasing that it was produced locally and therefore should not be considered to have sufficient
maturity to enter production. It is possible to override this behaviour by setting the 'MRMAT_VERSION' environment
variable to whatever version is desired but clearly doing so is discouraged.

### As part of a CI build

GitHub Actions will trigger a build upon a push and as part of a pull request. If the build is the result of a merge onto the merge branch then it is considered to be a release build, which will
cause a tag to be created. The version is suffixed with '-SNAPSHOT' for any non-release build.

The build version is relayed via the 'MRMAT_VERSION' environment variable from the 'MAJOR', 'MINOR' operational
variables as well as the 'GITHUB_RUN_NUMBER'. 'MAJOR' and 'MINOR' are meant to be adjusted manually because those are
conscious version bumps that are expected to happen far less frequently than individual builds. The 'GITHUB_RUN_NUMBER'
is injected by GitHub Actions itself, resulting in a discrete version of the product for each build.

## How to hack on this

### Auto-Versioning

The version of the product is auto-generated during build-time as explained above, but it is deliberately relayed to the
product runtime as well through the generated code that the 'generateVersion' Gradle task that will produce a 'Version'
class whose companion object holds it for runtime consumption. There is a test case to ensure that the version is
available in this way.

