package org.mrmat.plugins.ansible

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class AnsibleAppModel(
    @JsonProperty("app_name")
    var appName: String,

    @JsonProperty("app_version")
    var appVersion: String
)
