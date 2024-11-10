package cz.wexom.eds.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class AppProperties(
    var providers: MutableMap<String, ProviderProperties> = mutableMapOf()
)

data class ProviderProperties (
    var enabled: Boolean = false,
    var url: String = "",
)
