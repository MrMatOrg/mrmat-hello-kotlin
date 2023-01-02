plugins {
    id("mrmat.helm-conventions")
}

description = "The Helm chart deploying the mrmat-hello-kotlin containers"

dependencies {
    project(":apps:helloworld-spring")
}

configurations {
    create("containers") {
        attributes {
            attribute(
                LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
                objects.named(LibraryElements::class.java, "bootArchives"))
        }
    }
}

val instrumentedHelm: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named("helm-chart"))
    }
}
