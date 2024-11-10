package cz.wexom.eds.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import cz.wexom.eds.domain.ResponseWrapper
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthenticationErrorHandler : ServerAuthenticationEntryPoint {
    override fun commence(exchange: ServerWebExchange, ex: AuthenticationException): Mono<Void> {
        val response = exchange.response
        response.statusCode = HttpStatus.UNAUTHORIZED
        val bufferFactory = response.bufferFactory()
        val dataBuffer = bufferFactory.wrap(
            jacksonObjectMapper().writeValueAsBytes(
                ResponseWrapper.Error(
                    cause = ex.javaClass.name,
                    message = ex.message ?: HttpStatus.UNAUTHORIZED.reasonPhrase
                )
            )
        )

        return response.writeWith(Mono.just(dataBuffer))
    }
}