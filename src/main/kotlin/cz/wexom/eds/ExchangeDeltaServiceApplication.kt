package cz.wexom.eds

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExchangeDeltaServiceApplication

fun main(args: Array<String>) {
    runApplication<ExchangeDeltaServiceApplication>(*args)
}
