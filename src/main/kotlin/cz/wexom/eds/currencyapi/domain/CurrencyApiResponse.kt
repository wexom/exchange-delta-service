package cz.wexom.eds.currencyapi.domain

import java.time.LocalDate

data class CurrencyApiResponse (
    val date: LocalDate,
    val czk: Map<String, Double>
)