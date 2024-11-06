package cz.wexom.eds.domain

data class ExchangeRateDelta(
    val delta: Double,
    val best: String,
    val worst: String,
    val data: Set<ExchangeRateProvider>,
) {
    data class ExchangeRateProvider(
        val provider: String,
        val exchangeRate: Double
    )
}
