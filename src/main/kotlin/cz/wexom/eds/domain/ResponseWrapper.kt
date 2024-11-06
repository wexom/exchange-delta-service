package cz.wexom.eds.domain

data class ResponseWrapper<T>(
    val data: T? = null,
    val error: Error? = null,
) {
    data class Error(
        val cause: String,
        val message: String,
    )
}
