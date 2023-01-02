plugins {
    id("mrmat.ansible-conventions")
}

description = "The Helm chart deploying the mrmat-hello-kotlin containers"

dependencies {
    project(":deployment:helm")
}

configurations {
    create("containers") {
        attributes {
            attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named("helm-chart"))
        }
    }
}
