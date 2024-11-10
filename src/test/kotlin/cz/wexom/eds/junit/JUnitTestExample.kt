package cz.wexom.eds.junit

import cz.wexom.eds.cnb.CNBProvider
import cz.wexom.eds.currencyapi.CurrencyApiProvider
import cz.wexom.eds.domain.ExchangePair
import cz.wexom.eds.service.ExchangeRateService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class JUnitTestExample {

    private lateinit var exchangeRateService: ExchangeRateService
    private lateinit var cnbProvider: CNBProvider
    private lateinit var currencyApiProvider: CurrencyApiProvider

    @BeforeEach
    fun setUp() {
        cnbProvider = mockk<CNBProvider>()
        currencyApiProvider = mockk<CurrencyApiProvider>()
        exchangeRateService = ExchangeRateService(cnbProvider, currencyApiProvider)
    }

    @Test
    @DisplayName("test supported currency pairs for two providers, with one provider having less pairs")
    fun test01() {
        runBlocking {
            every { runBlocking { cnbProvider.getExchangePairs() }  } returns setOf(ExchangePair("CZK", "EUR"), ExchangePair("CZK", "USD"))
            every { runBlocking { currencyApiProvider.getExchangePairs() } } returns setOf(ExchangePair("CZK", "USD"))
            val supportedCurrencyPairs = exchangeRateService.getSupportedCurrencyPairs(null)

            Assertions.assertEquals(setOf(ExchangePair("CZK", "USD")), supportedCurrencyPairs)
            verify(exactly = 1) { runBlocking { cnbProvider.getExchangePairs() } }
            verify(exactly = 1) { runBlocking { currencyApiProvider.getExchangePairs() } }
        }
    }

    @Test
    @DisplayName("test supported currency pairs for CNB provider")
    fun test02() {
        runBlocking {
            every { runBlocking { cnbProvider.getExchangePairs() }  } returns setOf(ExchangePair("CZK", "EUR"), ExchangePair("CZK", "USD"))
            val supportedCurrencyPairs = exchangeRateService.getSupportedCurrencyPairs("CNB")

            Assertions.assertEquals(setOf(ExchangePair("CZK", "EUR"), ExchangePair("CZK", "USD")), supportedCurrencyPairs)
            verify(exactly = 1) { runBlocking { cnbProvider.getExchangePairs() } }
            verify(exactly = 0) { runBlocking { currencyApiProvider.getExchangePairs() } }
        }
    }
}