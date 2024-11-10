package cz.wexom.eds.currencyapi

import cz.wexom.eds.configuration.properties.AppProperties
import cz.wexom.eds.currencyapi.domain.CurrencyApiResponse
import cz.wexom.eds.infrastructure.Client
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class CurrencyApiClient(private val client: WebClient, private val appProperties: AppProperties) : Client {
    suspend fun getTodayExchangeRates() =client.get()
        .uri(appProperties.providers["currency-api"]!!.url)
            .retrieve()
            .awaitBody<CurrencyApiResponse>()

    override suspend fun isHealthy() = kotlin.runCatching { getTodayExchangeRates() }.isSuccess
}