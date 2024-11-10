package cz.wexom.eds.service

import cz.wexom.eds.cnb.CNBProvider
import cz.wexom.eds.currencyapi.CurrencyApiProvider
import cz.wexom.eds.domain.ExchangePair
import cz.wexom.eds.domain.ExchangeRateDelta
import cz.wexom.eds.domain.Provider
import cz.wexom.eds.exception.ProviderNotFoundException
import org.springframework.stereotype.Service
import kotlin.math.abs

@Service
class ExchangeRateService(
    private val cnbProvider: CNBProvider,
    private val currencyApiProvider: CurrencyApiProvider,
) {
    fun getProviders() = setOf(Provider.CNB, Provider.CURRENCY_API)

    suspend fun getSupportedCurrencyPairs(provider: String?): Set<ExchangePair> {
        return when (provider) {
            null -> currencyApiProvider.getExchangePairs().intersect(cnbProvider.getExchangePairs())
            Provider.CNB.name -> cnbProvider.getExchangePairs()
            Provider.CURRENCY_API.name -> currencyApiProvider.getExchangePairs()
            else -> throw ProviderNotFoundException("Unknown provider: $provider")
        }
    }

    suspend fun getSupportedCurrencyPairsDelta(from: String, to: String): ExchangeRateDelta {
        val exchangeRateProviders = setOf(
            ExchangeRateDelta.ExchangeRateProvider(
                Provider.CNB.name,
                cnbProvider.getExchangeRate(from, to),
            ),
            ExchangeRateDelta.ExchangeRateProvider(
                Provider.CURRENCY_API.name,
                currencyApiProvider.getExchangeRate(from, to),
            ),
        )

        return ExchangeRateDelta(
            delta = abs(cnbProvider.getExchangeRate(from, to) - currencyApiProvider.getExchangeRate(from, to)),
            best = exchangeRateProviders.maxBy { it.exchangeRate }.provider,
            worst = exchangeRateProviders.minBy { it.exchangeRate }.provider,
            data = exchangeRateProviders,
        )
    }
}