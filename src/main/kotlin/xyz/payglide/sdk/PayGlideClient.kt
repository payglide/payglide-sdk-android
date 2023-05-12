package xyz.payglide.sdk

import xyz.payglide.sdk.data.api.PaymentApi
import xyz.payglide.sdk.data.model.InitPaymentRequest
import xyz.payglide.sdk.data.model.PaymentSession
import xyz.payglide.sdk.infrastructure.ApiClient

/**
 * Configuration for the PayGlideClient
 * @param apiKey PayGlide API key.
 * @param url Base URL
 * @param token the access token
 * @param username Username for basic auth
 * @param password Password for basic auth
 * @param pollInterval Poll interval
 * @param pollTimeout Poll timeout
 */
data class ClientConfig(
    /**
     * API key
     */
    val apiKey: String? = null,
    /**
     * Base URL
     */
    val url: String? = null,
    /**
     * Access token
     */
    val token: String? = null,
    /**
     * Username for basic auth
     */
    val username: String? = null,
    /**
     * Password for basic auth
     */
    val password: String? = null,
    /**
     * Milliseconds interval between calling the session service when using
     * the onTransactionComplete() method.
     * The call is repeated until the transaction is completed,
     * an error is returned, or the poll timeout is reached.
     *
     * Default 5 seconds.
     */
    val pollInterval: Long? = null,
    /**
     * Max time in milliseconds to keep polling if no transaction completed
     * status is returned.
     * If the transaction completed status is not received before the timeout
     * then the onTransactionComplete method rejects with a timeout error.
     *
     * Default 300 seconds.
     */
    val pollTimeout: Long? = null,
)

/**
 * A client for PayGlide's API
 *
 * This client provides convenience methods to make requests to the PayGlide API.
 *
 * @param ClientConfig
 */
class PayGlideClient(config: ClientConfig?) {

    private val pollInterval: Long
    private val pollTimeout: Long
    private val paymentApi: PaymentApi

    init {
        if (config?.url != null) {
            paymentApi = PaymentApi(config.url)
        } else {
            paymentApi = PaymentApi()
        }
        if (config?.apiKey != null) {
            ApiClient.apiKey["x-api-key"] = config.apiKey
        }
        ApiClient.accessToken = config?.token
        ApiClient.username = config?.username
        ApiClient.password = config?.password
        this.pollInterval = config?.pollInterval ?: (5 * 1000)
        this.pollTimeout = config?.pollTimeout ?: (300 * 1000)
    }

    /**
     * Initialise a payment
     * Create a new payment object based on the transaction data
     *
     * @param initPaymentRequest Data required to intialise a payment
     * @return Payment - provides method to poll the payment status
     */
    suspend fun initPayment(initPaymentRequest: InitPaymentRequest): Payment {
        return Payment(
            session = paymentApi.initPayment(initPaymentRequest),
            pollInterval = this.pollInterval,
            pollTimeout = this.pollTimeout,
            paymentApi = this.paymentApi,
        )
    }

    /**
     * Initialise a payment session
     * Create a new payment session based on the transaction data
     *
     * @param initPaymentRequest Data required to intialise a payment
     * @return PaymentSession - payment session object
     */
    suspend fun initPaymentSession(initPaymentRequest: InitPaymentRequest): PaymentSession {
        return paymentApi.initPayment(initPaymentRequest)
    }

    /**
     * Returns the payment session information
     * @param sessionId - the ID of the payment session
     * @return PaymentSession - payment session object
     */
    suspend fun getSessionById(sessionId: String): PaymentSession {
        return paymentApi.getSessionById(sessionId)
    }
}
