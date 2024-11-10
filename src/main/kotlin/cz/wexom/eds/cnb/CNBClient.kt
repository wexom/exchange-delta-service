package cz.wexom.eds.cnb

import cz.wexom.eds.configuration.properties.AppProperties
import cz.wexom.eds.infrastructure.Client
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class CNBClient(
    private val client: WebClient,
    private val appProperties: AppProperties,
    properties: AppProperties
) : Client {
    suspend fun getTodayExchangeRates(): String {
        return client.get().uri(appProperties.providers["cnb"]!!.url).retrieve().awaitBody()
    }

    override suspend fun isHealthy() = kotlin.runCatching { getTodayExchangeRates() }.isSuccess
}