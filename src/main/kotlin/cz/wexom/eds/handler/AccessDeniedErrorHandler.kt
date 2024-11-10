package cz.wexom.eds.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import cz.wexom.eds.domain.ResponseWrapper
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AccessDeniedErrorHandler : ServerAccessDeniedHandler {
    override fun handle(exchange: ServerWebExchange, denied: AccessDeniedException): Mono<Void> {
        val response = exchange.response
        response.statusCode = HttpStatus.FORBIDDEN
        val bufferFactory = response.bufferFactory()
        val dataBuffer = bufferFactory.wrap(
            jacksonObjectMapper().writeValueAsBytes(
                ResponseWrapper.Error(
                    cause = denied.javaClass.name,
                    message = denied.message ?: HttpStatus.FORBIDDEN.reasonPhrase
                )
            )
        )

        return response.writeWith(Mono.just(dataBuffer))
    }

}