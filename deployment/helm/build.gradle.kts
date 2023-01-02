plugins {
    id("mrmat.helm-conventions")
}

description = "The Helm chart deploying the mrmat-hello-kotlin containers"


dependencies {
    project(":apps:versioning")
}
