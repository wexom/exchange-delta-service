package cz.wexom.eds.domain

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Generic wrapper for API responses, containing either data or an error.")
data class ResponseWrapper<T>(
    @field:Schema(description = "The response data, if the request was successful. Can be object, list, etc.", example = "null", nullable = true)
    val data: T? = null,
    @field:Schema(description = "Error details, if the request encountered an issue.", nullable = true)
    val error: Error? = null,
) {
    @Schema(description = "Error details in case of a failed request.")
    data class Error(
        @field:Schema(description = "The cause of the error.", nullable = false, example = "Unexpected error")
        val cause: String,
        @field:Schema(description = "A detailed message describing the error.", nullable = false, example = "An unexpected error occurred while processing the request.")
        val message: String,
    )
}
