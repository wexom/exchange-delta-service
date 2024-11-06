package cz.wexom.eds.cnb.domain

data class CNBResponse (
    val country: String,
    val currency: String,
    val amount: Short,
    val code: String,
    val exchangeRate: Double,
)