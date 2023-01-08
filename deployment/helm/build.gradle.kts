plugins {
    id("org.mrmat.plugins.version")
    id("org.mrmat.plugins.helm")
}

dependencies {
    project(":apps:helloworld-spring")
}
