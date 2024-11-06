package cz.wexom.eds.api

import cz.wexom.eds.domain.ResponseWrapper
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

class ReactiveExceptionHandler(
    private val exceptionToStatusCode: Map<Class<out Exception>, HttpStatus>,
    private val defaultStatus: HttpStatus,
    errorAttributes: ErrorAttributes,
    resources: WebProperties.Resources,
    applicationContext: ApplicationContext
) : AbstractErrorWebExceptionHandler(errorAttributes, resources, applicationContext) {

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?) =
        RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)

    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        val error: Throwable = getError(request)
        val httpStatus: HttpStatus = when (error) {
            is ResponseStatusException -> HttpStatus.valueOf(error.statusCode.value())
            is Exception -> exceptionToStatusCode[error::class.java] ?: defaultStatus
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        val errorResponse = ResponseWrapper<Any>(
            error = ResponseWrapper.Error(
                cause = error.javaClass.name ?: error.cause?.javaClass?.name ?: "Unknown",
                message = error.message ?: httpStatus.reasonPhrase
            )
        )

        return ServerResponse
            .status(httpStatus)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(errorResponse))
    }
}