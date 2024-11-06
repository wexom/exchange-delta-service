package cz.wexom.eds.cnb

import cz.wexom.eds.infrastructure.Client
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class CNBClient(private val client: WebClient) : Client {
    suspend fun getTodayExchangeRates(): String {
        return client.get().uri("https://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.txt").retrieve().awaitBody()
    }

    override suspend fun isHealthy() = kotlin.runCatching { getTodayExchangeRates() }.isSuccess
}