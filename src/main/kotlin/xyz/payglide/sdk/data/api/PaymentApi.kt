@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport",
)

package xyz.payglide.sdk.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.payglide.sdk.data.model.InitPaymentRequest
import xyz.payglide.sdk.data.model.PaymentSession
import xyz.payglide.sdk.infrastructure.ApiClient
import xyz.payglide.sdk.infrastructure.ClientError
import xyz.payglide.sdk.infrastructure.ClientException
import xyz.payglide.sdk.infrastructure.MultiValueMap
import xyz.payglide.sdk.infrastructure.RequestConfig
import xyz.payglide.sdk.infrastructure.RequestMethod
import xyz.payglide.sdk.infrastructure.ResponseType
import xyz.payglide.sdk.infrastructure.ServerError
import xyz.payglide.sdk.infrastructure.ServerException
import xyz.payglide.sdk.infrastructure.Success

class PaymentApi(basePath: kotlin.String = defaultBasePath) : ApiClient(basePath) {
    companion object {
        @JvmStatic
        val defaultBasePath: String by lazy {
            System.getProperties().getProperty("xyz.payglide.sdk.baseUrl", "https://test.payglide.io/api")
        }
    }

    /**
     * Find session by ID
     * Returns the session information
     * @param sessionId ID of session to return * @return PaymentSession
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(UnsupportedOperationException::class, ClientException::class, ServerException::class)
    suspend fun getSessionById(sessionId: kotlin.String): PaymentSession = withContext(Dispatchers.IO) {
        val localVariableConfig = getSessionByIdRequestConfig(sessionId = sessionId)

        val localVarResponse = request<Unit, PaymentSession>(
            localVariableConfig,
        )

        return@withContext when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as PaymentSession
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> {
                val localVarError = localVarResponse as ClientError<*>
                throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
            ResponseType.ServerError -> {
                val localVarError = localVarResponse as ServerError<*>
                throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
        }
    }

    /**
     * To obtain the request config of the operation getSessionById
     *
     * @param sessionId ID of session to return * @return RequestConfig
     */
    fun getSessionByIdRequestConfig(sessionId: kotlin.String): RequestConfig<Unit> {
        val localVariableBody = null
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()

        return RequestConfig(
            method = RequestMethod.GET,
            path = "/payment-session/{sessionId}".replace("{" + "sessionId" + "}", "$sessionId"),
            query = localVariableQuery,
            headers = localVariableHeaders,
            body = localVariableBody,
        )
    }

    /**
     * Initialise a payment
     * Create a new payment session based on the transaction data
     * @param initPaymentRequest Initialise a payment * @return PaymentSession
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(UnsupportedOperationException::class, ClientException::class, ServerException::class)
    suspend fun initPayment(initPaymentRequest: InitPaymentRequest): PaymentSession = withContext(Dispatchers.IO) {
        val localVariableConfig = initPaymentRequestConfig(initPaymentRequest = initPaymentRequest)

        val localVarResponse = request<InitPaymentRequest, PaymentSession>(
            localVariableConfig,
        )

        return@withContext when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as PaymentSession
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> {
                val localVarError = localVarResponse as ClientError<*>
                throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
            ResponseType.ServerError -> {
                val localVarError = localVarResponse as ServerError<*>
                throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
        }
    }

    /**
     * To obtain the request config of the operation initPayment
     *
     * @param initPaymentRequest Initialise a payment * @return RequestConfig
     */
    fun initPaymentRequestConfig(initPaymentRequest: InitPaymentRequest): RequestConfig<InitPaymentRequest> {
        val localVariableBody = initPaymentRequest
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()

        return RequestConfig(
            method = RequestMethod.POST,
            path = "/init-payment",
            query = localVariableQuery,
            headers = localVariableHeaders,
            body = localVariableBody,
        )
    }
}
