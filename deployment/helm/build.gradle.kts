plugins {
    id("org.mrmat.plugins.version")
    id("org.mrmat.plugins.helm")
}

dependencies {
    project(":helloworld-spring")
}

val report = project.configurations.create("report") {
    description = "A test"
    isCanBeConsumed = true
    isCanBeResolved = false
}

project.artifacts.add("report", project.layout.buildDirectory.file("reports/ansible-lint.txt"))

