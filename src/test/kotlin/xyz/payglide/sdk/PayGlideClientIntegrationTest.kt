package xyz.payglide.sdk

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import xyz.payglide.sdk.data.model.Argument
import xyz.payglide.sdk.data.model.InitPaymentRequest
import xyz.payglide.sdk.data.model.PaymentSession
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import xyz.payglide.sdk.infrastructure.ClientException
import xyz.payglide.sdk.infrastructure.ServerException
import java.lang.UnsupportedOperationException

@TestInstance(PER_CLASS)
class PayGlideClientIntegrationTest {

    val A_SESSION_ID = "23999587-b312-4e1f-950e-5e95984fea44"
    val AN_AMOUNT = "13"
    val A_CURRENCY = "USDC"
    val AN_ADDRESS = "0x6f9ce307c114f443"
    val A_HASH = "cea6b2d18abce452e0b57a7393642a9358de23ebd5dffa1589563162d0ab44c4"
    val VALID_PAYMENT_REQUEST = InitPaymentRequest(
        address = "0x6f9ce307c114f443",
        code = "test script",
        arguments = listOf(
            Argument(
                value = "1.23",
                type = "UFix64",
            ),
        ),
    )

    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: PayGlideClient

    @BeforeAll
    fun setup() {
        mockWebServer = MockWebServer()
        println(mockWebServer)

        client = PayGlideClient(ClientConfig(
            url = mockWebServer.url("/").toString(),
            apiKey = "your api key",
        ))
    }

    @AfterAll
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should be able to create a client`() {
        assertDoesNotThrow {
            val client = PayGlideClient(ClientConfig(
                url = "https://test.payglide.io/api",
                apiKey = "1234567890",
                token = "token",
                username = "username",
                password = "password",
                pollInterval = 5000,
                pollTimeout = 10000,
            ))
        }
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should be able to return a session`() = runTest {

        val expectedSession = PaymentSession(
            id = A_SESSION_ID,
            amount = AN_AMOUNT,
            currency = A_CURRENCY,
            address = AN_ADDRESS,
            status = PaymentSession.Status.TOKEN_TRANSFER_COMPLETED,
            transactionHash = A_HASH,
        )

        val json = Json.encodeToString(expectedSession)

        val response = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(json)
            .setResponseCode(200)

        mockWebServer.enqueue(response)

        val paymentSession = client.getSessionById(A_SESSION_ID)
        Assertions.assertEquals(expectedSession, paymentSession)
    }


    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should be able to initialise a payment session`() = runTest {
        val expectedSession = PaymentSession(
            id = A_SESSION_ID,
            amount = AN_AMOUNT,
            currency = A_CURRENCY,
            address = AN_ADDRESS,
            status = PaymentSession.Status.PAYMENT_INITIATED,
            transactionHash = A_HASH,
        )

        val json = Json.encodeToString(expectedSession)

        val response = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(json)
            .setResponseCode(200)

        mockWebServer.enqueue(response)

        val paymentSession = client.initPaymentSession(VALID_PAYMENT_REQUEST)

        Assertions.assertEquals(expectedSession, paymentSession)
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should be able to initialise a payment and check payment related information`() = runTest {
        val expectedSession = PaymentSession(
            id = A_SESSION_ID,
            amount = AN_AMOUNT,
            currency = A_CURRENCY,
            address = AN_ADDRESS,
            status = PaymentSession.Status.PAYMENT_INITIATED,
            transactionHash = A_HASH,
        )

        val json = Json.encodeToString(expectedSession)

        val response = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(json)
            .setResponseCode(200)

        mockWebServer.enqueue(response)

        val payment = client.initPayment(VALID_PAYMENT_REQUEST)

        Assertions.assertEquals(expectedSession.id, payment.sessionId)
        Assertions.assertEquals(expectedSession.address, payment.address)
        Assertions.assertEquals(expectedSession.amount, payment.amount)
        Assertions.assertEquals(expectedSession.currency, payment.currency)
        Assertions.assertEquals(expectedSession.transactionHash, payment.transactionHash)
        Assertions.assertEquals("https://test.payglide.io/wallet-pay?sessionId=23999587-b312-4e1f-950e-5e95984fea44", payment.getPaymentPage())

        mockWebServer.enqueue(response)
        val currentStatus = payment.getCurrentStatus()
        Assertions.assertEquals(PaymentSession.Status.PAYMENT_INITIATED, currentStatus)
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should throw client error when 4xx is returned for init payment request`() = runTest {
        assertThrows<ClientException> {
            val response = MockResponse()
                .setResponseCode(400)

            mockWebServer.enqueue(response)

            client.initPaymentSession(VALID_PAYMENT_REQUEST)
        }
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should throw server error when 5xx is returned for init payment request`() = runTest {
        assertThrows<ServerException> {
            val response = MockResponse()
                .setResponseCode(502)

            mockWebServer.enqueue(response)

            client.initPaymentSession(VALID_PAYMENT_REQUEST)
        }
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should throw UnsupportedOperationException when 3xx is returned for init payment request`() = runTest {
        assertThrows<UnsupportedOperationException> {
            val response = MockResponse()
                .setResponseCode(302)

            mockWebServer.enqueue(response)

            client.initPaymentSession(VALID_PAYMENT_REQUEST)
        }
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should throw UnsupportedOperationException when 1xx is returned for init payment request`() = runTest {
        assertThrows<UnsupportedOperationException> {
            val response = MockResponse()
                .setResponseCode(102)

            mockWebServer.enqueue(response)

            client.initPaymentSession(VALID_PAYMENT_REQUEST)
        }
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should throw client error when 4xx is returned for get session by ID`() = runTest {
        assertThrows<ClientException> {
            val response = MockResponse()
                .setResponseCode(400)

            mockWebServer.enqueue(response)

            client.getSessionById(A_SESSION_ID)
        }
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should throw server error when 5xx is returned for get session by ID`() = runTest {
        assertThrows<ServerException> {
            val response = MockResponse()
                .setResponseCode(502)

            mockWebServer.enqueue(response)

            client.getSessionById(A_SESSION_ID)
        }
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should throw UnsupportedOperationException when 3xx is returned for get session by ID`() = runTest {
        assertThrows<UnsupportedOperationException> {
            val response = MockResponse()
                .setResponseCode(302)

            mockWebServer.enqueue(response)

            client.getSessionById(A_SESSION_ID)
        }
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should throw UnsupportedOperationException when 1xx is returned for get session by ID`() = runTest {
        assertThrows<UnsupportedOperationException> {
            val response = MockResponse()
                .setResponseCode(102)

            mockWebServer.enqueue(response)

            client.getSessionById(A_SESSION_ID)
        }
    }
}
