package org.mrmat.plugins.ansible

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

abstract class AnsiblePlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.apply(DistributionPlugin::class.java)
        project.pluginManager.apply(AnsibleBasePlugin::class.java)
        val ansibleExtension = project.extensions.getByType<AnsibleExtension>()

        //
        // Opinionated task configuration and weaving

        project.tasks.named<Copy>("ansibleAssemble") {
            dependsOn(project.tasks.named("ansibleVersion"))
        }

        project.tasks.named<Exec>("ansibleLint") {
            dependsOn(project.tasks.named<Copy>("ansibleAssemble"))
        }

        project.tasks.named<Exec>("ansibleDeploy") {
            dependsOn(project.tasks.named<Copy>("ansibleAssemble"))
        }

        //
        // Declare the plugin output artefact

        val ansibleConfiguration = project.configurations.create(ansibleExtension.configurationName.get()) {
            description = "The assembled Ansible Playbook"
            isCanBeConsumed = true
            isCanBeResolved = false
        }
        val distributions: DistributionContainer = project.extensions.getByType<DistributionContainer>(DistributionContainer::class.java)
        distributions.create(ansibleExtension.distributionName.get()) {
            distributionBaseName.set(ansibleExtension.distributionName)
            contents {
                from(project.tasks.named<Copy>("ansibleAssemble"))
            }
        }
        val distributionTarTask = project.tasks.named<Tar>("${ansibleExtension.distributionName.get()}DistTar")
        project.artifacts.add(ansibleConfiguration.name, distributionTarTask)

        // You may be tempted to set a dependsOn("helmAssemble") here but it is implicitly stated
        // by the distribution configuration below (whose contents come from helmAssemble)
        distributionTarTask.configure {
            compression = Compression.GZIP
            archiveExtension.set("tar.gz")
        }

        project.tasks.named("check") {
            dependsOn(project.tasks.named<Exec>("ansibleLint"))
        }

        project.tasks.named("build") {
            dependsOn(distributionTarTask)
        }
    }
}
