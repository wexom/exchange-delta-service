package cz.wexom.eds.api

import cz.wexom.eds.domain.ResponseWrapper
import cz.wexom.eds.service.ExchangeRateService
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tags(value = [Tag(name = "Exchange rate API")])
@RestController
@RequestMapping("/v1/exchange")
class ExchangeRateApi(private val exchangeRateService: ExchangeRateService) {
    @GetMapping("/providers")
    @PreAuthorize("hasRole('USER')")
    suspend fun getProviders() = ResponseWrapper(data = exchangeRateService.getProviders())

    @GetMapping("/providers/currencies/pairs")
    @PreAuthorize("hasRole('USER')")
    suspend fun getSupportedCurrencyPairs(@RequestParam("provider") provider: String?) =
        ResponseWrapper(data = exchangeRateService.getSupportedCurrencyPairs(provider?.uppercase()))

    @GetMapping("/providers/currencies/pairs/delta")
    @PreAuthorize("hasRole('ADMIN')")
    suspend fun getSupportedCurrencyPairsDelta(@RequestParam("to") to: String, @RequestParam("from") from: String?) =
        ResponseWrapper(data = exchangeRateService.getSupportedCurrencyPairsDelta(from?.uppercase() ?: "CZK", to.uppercase()))
}