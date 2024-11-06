package cz.wexom.eds.configuration

import cz.wexom.eds.api.ReactiveExceptionHandler
import cz.wexom.eds.cnb.CNBClient
import cz.wexom.eds.currencyapi.CurrencyApiClient
import cz.wexom.eds.exception.ProviderNotFoundException
import cz.wexom.eds.health.ExtendedHealthIndicator
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerCodecConfigurer

@Configuration
class AppConfig {

    @Bean
    @Order(-2)
    fun reactiveExceptionHandler(
        exceptionToStatusCode: Map<Class<out Exception>, HttpStatus>,
        webProperties: WebProperties,
        applicationContext: ApplicationContext,
        configurer: ServerCodecConfigurer
    ): ReactiveExceptionHandler {
        val exceptionHandler = ReactiveExceptionHandler(
            exceptionToStatusCode,
            HttpStatus.INTERNAL_SERVER_ERROR,
            DefaultErrorAttributes(),
            webProperties.resources,
            applicationContext
        )
        exceptionHandler.setMessageReaders(configurer.readers)
        exceptionHandler.setMessageWriters(configurer.writers)
        return exceptionHandler
    }

    @Bean
    fun exceptionToStatusCode(): Map<Class<out Exception>, HttpStatus> {
        return mapOf(
            IllegalStateException::class.java to HttpStatus.INTERNAL_SERVER_ERROR,
            ProviderNotFoundException::class.java to HttpStatus.NOT_FOUND,
        )
    }

    @Bean
    fun extendedHealthIndicator(cnbClient: CNBClient, currencyApiClient: CurrencyApiClient): ExtendedHealthIndicator {
        return ExtendedHealthIndicator(cnbClient, currencyApiClient)
    }
}