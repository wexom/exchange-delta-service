package cz.wexom.eds.domain

data class Provider(
    val name: String,
) {
    companion object {
        val CNB = Provider("CNB")
        val CURRENCY_API = Provider("CURRENCY_API")
    }
}
