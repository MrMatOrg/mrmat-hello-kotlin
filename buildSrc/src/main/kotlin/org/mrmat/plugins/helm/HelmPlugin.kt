package org.mrmat.plugins.helm

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.distribution.DistributionContainer
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Compression
import org.gradle.api.tasks.bundling.Tar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

abstract class HelmPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply(DistributionPlugin::class.java)
        project.plugins.apply(HelmBasePlugin::class.java)
        val helmExtension = project.extensions.getByType<HelmExtension>()

        //
        // Opinionated task configuration and weaving

        project.tasks.named<Copy>("helmAssemble") {
            dependsOn(project.tasks.named<HelmVersionTask>("helmVersion"))
        }

        project.tasks.named<Exec>("helmLint") {
            dependsOn(project.tasks.named<Copy>("helmAssemble"))
        }

        project.tasks.named<Exec>("helmDeployLocal") {
            dependsOn(project.tasks.named<Copy>("helmAssemble"))
        }

        //
        // Declare the plugin output artefact

        val helmConfiguration = project.configurations.create(helmExtension.configurationName.get()) {
            description = "The assembled Helm chart"
            isCanBeConsumed = true
            isCanBeResolved = false
        }
        val distributions: DistributionContainer = project.extensions.getByType<DistributionContainer>(DistributionContainer::class.java)
        distributions.create(helmExtension.configurationName.get()) {
            distributionBaseName.set(helmExtension.configurationName)
            contents {
                from(helmExtension.buildPath)
            }
        }
        val distributionTarTask = project.tasks.named<Tar>("${helmExtension.distributionName.get()}DistTar")
        project.artifacts.add(helmConfiguration.name, distributionTarTask)

        // You may be tempted to set a dependsOn("helmAssemble") here but it is implicitly stated
        // by the distribution configuration below (whose contents come from helmAssemble)
        distributionTarTask.configure {
            compression = Compression.GZIP
            archiveExtension.set("tar.gz")
        }

        project.tasks.named("check") {
            dependsOn(project.tasks.named<Exec>("helmLint"))
        }

        project.tasks.named("build") {
            dependsOn(distributionTarTask)
        }
    }
}
