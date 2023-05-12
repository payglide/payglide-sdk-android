package xyz.payglide.sdk

import xyz.payglide.sdk.data.api.PaymentApi
import xyz.payglide.sdk.data.model.PaymentSession

class PaymentSessionException(message: String) : Exception(message)

class Payment(
    /**
     * PaymentSession object
     */
    session: PaymentSession,
    /**
     * Milliseconds interval between calling the session service when using
     * the onTransactionComplete() method.
     * The call is repeated until the transaction is completed,
     * an error is returned, or the poll timeout is reached.
     */
    pollInterval: Long,
    /**
     * Max time in milliseconds to keep polling if no transaction completed
     * status is returned.
     * If the transaction completed status is not received before the timeout
     * then the onTransactionComplete method rejects with a timeout error.
     */
    pollTimeout: Long,
    paymentApi: PaymentApi,
) {
    /**
     * Payment session ID
     */
    val sessionId: String

    /**
     * The amount of cryptocurrency required to run the transaction
     */
    val amount: String

    /**
     * The cryptocurrency used for the transaction
     */
    val currency: String

    /**
     * The wallet address used to execute the transaction
     */
    val address: String

    /**
     * Hash of the blockchain transaction the session was initialised with
     */
    val transactionHash: String

    private val pollInterval: Long
    private val pollTimeout: Long
    private val paymentApi: PaymentApi

    init {
        val errors = ArrayList<String>()
        if (session.id == null) {
            errors.add("Invalid Session ID: ${session.id}")
        }
        if (session.amount == null) {
            errors.add("Invalid amount: ${session.amount}")
        }
        if (session.currency == null) {
            errors.add("Invalid currency: ${session.currency}")
        }
        if (session.address == null) {
            errors.add("Invalid address: ${session.address}")
        }
        if (session.transactionHash == null) {
            errors.add("Invalid transactionHash: ${session.transactionHash}")
        }
        if (errors.isNotEmpty()) {
            throw PaymentSessionException("Found ${errors.size} errors in PaymentSession object:\n${errors.joinToString("\n")}")
        }
        sessionId = session.id!!
        amount = session.amount!!
        currency = session.currency!!
        address = session.address!!
        transactionHash = session.transactionHash!!
        this.pollInterval = pollInterval
        this.pollTimeout = pollTimeout
        this.paymentApi = paymentApi
    }

    /**
     * Function to retrieve payment session information periodically until
     * it resolves or rejects.
     *
     * It returns a promise that resolves with a PaymentSession object,
     * when the payment session status is transaction completed.
     *
     * The payment session API will be reinvoked in `pollInterval` until it times out.
     * It will throw an error if the polling interval is exceeded.
     */
    suspend fun onTransactionComplete(): PaymentSession {
        return asyncPoll(
            getPaymentSessionFn = paymentApi::getSessionById,
            sessionId = this.sessionId,
            expectedStatus = PaymentSession.Status.tOKENTRANSFERCOMPLETED,
            failureStatus = arrayOf(PaymentSession.Status.pAYMENTFAILED),
            pollInterval = pollInterval,
            pollTimeout = pollTimeout,
        )
    }

    /**
     * Returns the payment page url, that can be used in an iframe.
     */
    fun getPaymentPage(): String {
        return "https://test.payglide.io/wallet-pay?sessionId=${this.sessionId}"
    }

    /**
     * Returns the current payment session status
     */
    suspend fun getCurrentStatus(): PaymentSession.Status {
        val paymentSession = paymentApi.getSessionById(this.sessionId)
        if (paymentSession.status == null) {
            throw PaymentSessionException("Invalid payment session status returned from the server")
        } else {
            return paymentSession.status
        }
    }
}
