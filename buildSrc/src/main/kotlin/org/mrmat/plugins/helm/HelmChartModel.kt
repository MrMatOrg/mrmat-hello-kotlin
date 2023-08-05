package org.mrmat.plugins.helm

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class HelmDependencyModel(
    var name: String,
    var version: String,
    var repository: String?,
    var condition: String?,
    var tags: List<String>?,

    @JsonProperty("import-values")
    var importValues: List<String>,

    var alias: String?
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class HelmChartModel(
    var apiVersion: String = "v2",
    var name: String,
    var version: String,
    var kubeVersion: String?,
    var description: String?,
    var type: String?,
    var keywords: List<String>?,
    var home: String?,
    var sources: List<String?>?,
    var dependencies: List<HelmDependencyModel>?,
    var maintainers: List<String>?,
    var icon: String? = "http://localhost/no-icon.png",
    var appVersion: String?,
    var deprecated: Boolean?,
    var annotations: Map<String, String>?
)
