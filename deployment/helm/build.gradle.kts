plugins {
    id("org.mrmat.plugins.gradle.version")
    id("org.mrmat.plugins.gradle.helm")
}

dependencies {
    project(":helloworld-spring")
}

tasks.helmChartDistZip.configure {
    dependsOn(tasks.helmVersion, tasks.helmAssemble)
    mustRunAfter(tasks.helmVersion, tasks.helmAssemble)
}

tasks.helmChartDistTar.configure {
    dependsOn(tasks.helmVersion, tasks.helmAssemble)
    mustRunAfter(tasks.helmVersion, tasks.helmAssemble)
}

//val report = project.configurations.create("report") {
//    description = "A test"
//    isCanBeConsumed = true
//    isCanBeResolved = false
//}
//
//project.artifacts.add("report", project.layout.buildDirectory.file("reports/ansible-lint.txt"))

