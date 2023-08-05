package org.mrmat.plugins.helm

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType

abstract class HelmVersionTask: DefaultTask() {

    @get:Input
    abstract val version: Property<String>

    @get:Input
    abstract val appVersion: Property<String>

    @get:InputFile
    abstract val helmChartSrc: RegularFileProperty

    @get:OutputFile
    abstract val helmChartOutput: RegularFileProperty

    init {
        val helmExtension = project.extensions.getByType<HelmExtension>(HelmExtension::class.java)
        version.convention("${project.version}")
        appVersion.convention("${project.version}")
        helmChartSrc.convention(helmExtension.srcPath.file("Chart.yaml"))
        helmChartOutput.convention(helmExtension.buildPath.file("Chart.yaml"))
    }

    @TaskAction
    fun execute() {
        val mapper = YAMLMapper().registerKotlinModule()
        var helmChart: HelmChartModel = mapper.readValue(helmChartSrc.get().asFile, HelmChartModel::class.java)
        helmChart.version = version.get()
        helmChart.appVersion = version.get()
        mapper.writeValue(helmChartOutput.get().asFile, helmChart)
    }
}