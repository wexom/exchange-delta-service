package cz.wexom.eds.configuration

import cz.wexom.eds.handler.AccessDeniedErrorHandler
import cz.wexom.eds.handler.AuthenticationErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        accessDeniedErrorHandler: AccessDeniedErrorHandler,
        authenticationErrorHandler: AuthenticationErrorHandler
    ): SecurityWebFilterChain {
        http
            .authorizeExchange {
                it.pathMatchers(
                    "/actuator/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/webjars/swagger-ui/**"
                ).permitAll().anyExchange().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling {
                it.accessDeniedHandler(accessDeniedErrorHandler)
                it.authenticationEntryPoint(authenticationErrorHandler)
            }
        return http.build()
    }

    @Bean
    fun inMemoryUserDetailsManager(): ReactiveUserDetailsService {
        val testUser = User.withUsername("test")
            .password("{noop}test")  // No password encoder for simplicity
            .roles("USER")
            .build()

        val adminUser = User.withUsername("admin")
            .password("{noop}admin")  // No password encoder for simplicity
            .roles("ADMIN")
            .build()

        return MapReactiveUserDetailsService(testUser, adminUser)
    }
}