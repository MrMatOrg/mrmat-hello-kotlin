plugins {
    id("org.mrmat.plugins.version")
    id("org.mrmat.plugins.rpm")
}

dependencies {
    project(":helloworld-spring")
}

mrmatRPM {
    rpmSpec.set("hello-world.spec")
    rpmFile.set(buildPath.file("RPMS/aarch64/hello-world-1-1.aarch64.rpm"))
}

val report = project.configurations.create("report") {
    description = "A test"
    isCanBeConsumed = true
    isCanBeResolved = false
}

project.artifacts.add("report", project.layout.buildDirectory.file("reports/ansible-lint.txt"))
