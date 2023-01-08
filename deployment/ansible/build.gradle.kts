plugins {
    id("org.mrmat.plugins.version")
    id("org.mrmat.plugins.ansible")
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
