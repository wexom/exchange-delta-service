package cz.wexom.eds.health

import cz.wexom.eds.cnb.CNBClient
import cz.wexom.eds.currencyapi.CurrencyApiClient
import cz.wexom.eds.domain.Provider
import kotlinx.coroutines.reactor.mono
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.ReactiveHealthIndicator
import reactor.core.publisher.Mono

class ExtendedHealthIndicator(
    private val cnbClient: CNBClient,
    private val currencyApiClient: CurrencyApiClient,
) : ReactiveHealthIndicator {
    override fun health(): Mono<Health> = mono {
        Health.up().withDetails(
            mapOf(
                "providers" to mapOf(
                    Provider.CNB.name to getStatus(cnbClient.isHealthy()),
                    Provider.CURRENCY_API.name to getStatus(currencyApiClient.isHealthy()),
                )
            )
        ).build()
    }

    private fun getStatus(isHealthy: Boolean): String {
        return if (isHealthy) "UP" else "DOWN"
    }

}