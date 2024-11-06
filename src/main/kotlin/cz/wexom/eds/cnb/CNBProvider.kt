package cz.wexom.eds.cnb

import cz.wexom.eds.cnb.domain.CNBResponse
import cz.wexom.eds.domain.ExchangePair
import cz.wexom.eds.exception.CurrencyNotSupportedByCNBException
import org.springframework.stereotype.Component

@Component
class CNBProvider(private val cnbClient: CNBClient) {
    suspend fun getExchangePairs(): Set<ExchangePair> {
        return parseCnb(cnbClient.getTodayExchangeRates()).flatMap {
            setOf(
                ExchangePair(it.code, "CZK"),
                ExchangePair("CZK", it.code)
            )
        }.toSet()
    }

    suspend fun getExchangeRate(from: String, to: String): Double {
        val cnbResponse = parseCnb(cnbClient.getTodayExchangeRates())
            .firstOrNull {from == "CZK" && it.code == to || it.code == from && to == "CZK"}
            ?: throw CurrencyNotSupportedByCNBException(from, to)

        if (cnbResponse.code == from) {
            return convert(cnbResponse)
        }

        return calculate(cnbResponse)
    }

    private fun calculate(cnbResponse: CNBResponse) = 1.0 / convert(cnbResponse)

    private fun convert(cnbResponse: CNBResponse) = cnbResponse.exchangeRate / cnbResponse.amount

    private fun parseCnb(todayExchangeRates: String): List<CNBResponse> {
        return todayExchangeRates.split("\n")
            .drop(2)
            .dropLast(1)
            .map { line ->
                val columns = line.split("|")
                CNBResponse(
                    country = columns[0],
                    currency = columns[1],
                    amount = columns[2].toShort(),
                    code = columns[3],
                    exchangeRate = columns[4].replace(",", ".").toDouble()
                )
            }
    }
}