package cz.wexom.eds.integration

import cz.wexom.eds.domain.ExchangePair
import cz.wexom.eds.domain.ResponseWrapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockserver.client.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = ["classpath:application-test.properties"])
class IntegrationTestExample {

    @Autowired
    private lateinit var testClient: WebTestClient

    @AfterEach
    fun clearExpectations() {
        mockServerClient.clear(request())
    }

    @Test
    @DisplayName("test exchange providers api with user test")
    @WithMockUser(username = "test", password = "test", roles = ["USER"])
    fun test01() {
        testClient.get().uri("/v1/exchange/providers")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data").isArray
            .jsonPath("$.data[0].name").isEqualTo("CNB")
            .jsonPath("$.data[1].name").isEqualTo("CURRENCY_API")
    }

    @Test
    @DisplayName("test exchange providers pairs api with intersection for both providers with user test")
    @WithMockUser(username = "test", password = "test", roles = ["USER"])
    fun test02() {
        mockServerClient.`when`(request().withMethod("GET").withPath("/cnb"))
            .respond(
                response().withStatusCode(200).withContentType(MediaType.TEXT_PLAIN).withBody(
                    """08.11.2024 #218
                země|měna|množství|kód|kurz
                USA|dolar|1|USD|23,417
                
            """.trimIndent()
                )
            )

        mockServerClient.`when`(request().withMethod("GET").withPath("/currency-api"))
            .respond(
                response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody(
                    """
                {
                	"date": "2024-11-08",
                	"czk": {
                		"usd": 0.042465435,
                		"usdc": 0.042470568
                	}
                }
            """.trimIndent()
                )
            )

        val body = testClient.get().uri("/v1/exchange/providers/currencies/pairs")
            .exchange()
            .expectStatus().isOk
            .expectBody(object : ParameterizedTypeReference<ResponseWrapper<Set<ExchangePair>>>() {})
            .returnResult().responseBody

        Assertions.assertEquals(setOf(ExchangePair("CZK", "USD"), ExchangePair("USD", "CZK")), body?.data)
    }

    companion object {
        private lateinit var mockServer: ClientAndServer
        private lateinit var mockServerClient: MockServerClient

        @BeforeAll
        @JvmStatic
        fun startMockServer() {
            mockServer = ClientAndServer.startClientAndServer(1080)
            mockServerClient = MockServerClient("localhost", 1080)
        }

        @AfterAll
        @JvmStatic
        fun stopMockServer() {
            mockServer.stop()
        }
    }

}