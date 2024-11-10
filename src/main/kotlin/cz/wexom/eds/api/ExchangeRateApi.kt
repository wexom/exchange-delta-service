package cz.wexom.eds.api

import cz.wexom.eds.domain.Provider
import cz.wexom.eds.domain.ResponseWrapper
import cz.wexom.eds.service.ExchangeRateService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tags(value = [Tag(name = "Exchange rate API", description = "API for providing exchange rates and currency pairs from different providers")])
@RestController
@RequestMapping("/v1/exchange")
class ExchangeRateApi(private val exchangeRateService: ExchangeRateService) {
    @Operation(
        summary = "Get list of available exchange rate providers",
        description = "Fetches all available exchange rate providers",
        security = [SecurityRequirement(name = "basicAuth")]
    )
    @GetMapping("/providers")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    suspend fun getProviders() = ResponseWrapper(data = exchangeRateService.getProviders())

    @Operation(
        summary = "Get supported currency pairs for a provider",
        description = """
            Fetches supported currency pairs for a specific exchange rate provider.
            In case provider is not specified, all providers are considered 
            and result is intersection of pairs from all available providers.
        """,
        parameters = [
            Parameter(name = "provider", description = "Provider name to filter currency pairs. Available values: CURRENCY_API, CNB", required = false, example = "CNB")
        ],
        security = [SecurityRequirement(name = "basicAuth")]
    )
    @GetMapping("/providers/currencies/pairs")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    suspend fun getSupportedCurrencyPairs(@RequestParam("provider") provider: String?) =
        ResponseWrapper(data = exchangeRateService.getSupportedCurrencyPairs(provider?.uppercase()))

    @Operation(
        summary = "Get delta between currency pairs",
        description = "Fetches the delta between currency pairs for the specified date range",
        parameters = [
            Parameter(name = "to", description = "The target currency code", required = true, example = "USD"),
            Parameter(name = "from", description = "The base currency code", required = false, example = "CZK")
        ],
        security = [SecurityRequirement(name = "basicAuth")]
    )
    @GetMapping("/providers/currencies/pairs/delta")
    @PreAuthorize("hasRole('ADMIN')")
    suspend fun getSupportedCurrencyPairsDelta(@RequestParam("to") to: String, @RequestParam("from") from: String?) =
        ResponseWrapper(data = exchangeRateService.getSupportedCurrencyPairsDelta(from?.uppercase() ?: "CZK", to.uppercase()))
}