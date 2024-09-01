plugins {
    id("org.mrmat.plugins.gradle.version")
    id("org.mrmat.plugins.gradle.ansible")
}

//mrmatAnsible {
//    ansibleLintCommand.set("/opt/dyn/python/ansible/bin/ansible-lint")
//    ansiblePlaybookCommand.set("/opt/dyn/python/ansible/bin/ansible-playbook")
//}

val helmChart: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}

dependencies {
    helmChart(project(path = ":deployment:helm", configuration = "helmChart"))
}

tasks.create<Copy>("copyHelmChart") {
    from(configurations.named("helmChart"))
    into(layout.buildDirectory.dir("helm"))
}

val wheel = project.configurations.create("wheel") {
    description = "The Python wheel"
    isCanBeConsumed = true
    isCanBeResolved = false
}
project.artifacts.add("wheel", layout.projectDirectory.file("dist/$project.rootProject.name-$project.version-py3-none-any.whl"))
