package cz.wexom.eds.currencyapi

import cz.wexom.eds.currencyapi.domain.CurrencyApiResponse
import cz.wexom.eds.domain.ExchangePair
import cz.wexom.eds.exception.CurrencyNotSupportedByCurrencyApiException
import org.springframework.stereotype.Component

@Component
class CurrencyApiProvider(private val currencyApiClient: CurrencyApiClient) {
    suspend fun getExchangePairs(): Set<ExchangePair> {
        return parseCurrencyApiResponse(currencyApiClient.getTodayExchangeRates()).keys.flatMap {
            setOf(
                ExchangePair(it, "CZK"),
                ExchangePair("CZK", it.uppercase())
            )
        }.toSet()
    }

    suspend fun getExchangeRate(from: String, to: String): Double {
        val currencyApiResponse = parseCurrencyApiResponse(currencyApiClient.getTodayExchangeRates()).entries
            .firstOrNull {from == "CZK" && it.key.uppercase() == to || it.key.uppercase() == from && to == "CZK"}
            ?: throw CurrencyNotSupportedByCurrencyApiException(from, to)

        if (currencyApiResponse.key.uppercase() == to) {
            return currencyApiResponse.value
        }

        return 1.0 / currencyApiResponse.value
    }

    fun parseCurrencyApiResponse(currencyApiResponse: CurrencyApiResponse): Map<String, Double> {
        return currencyApiResponse.czk.filter { it.key.uppercase() != "CZK" }
    }
}