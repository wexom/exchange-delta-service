package cz.wexom.eds.currencyapi

import cz.wexom.eds.currencyapi.domain.CurrencyApiResponse
import cz.wexom.eds.infrastructure.Client
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class CurrencyApiClient(private val client: WebClient) : Client {
    suspend fun getTodayExchangeRates() =client.get()
            .uri("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/czk.json")
            .retrieve()
            .awaitBody<CurrencyApiResponse>()

    override suspend fun isHealthy() = kotlin.runCatching { getTodayExchangeRates() }.isSuccess
}