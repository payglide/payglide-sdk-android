package xyz.payglide.sdk

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeout
import xyz.payglide.sdk.data.model.PaymentSession
import xyz.payglide.sdk.infrastructure.ServerException

class PollTimeoutExeption(message: String) : Exception(message)

class SessionFailureStateException(message: String) : Exception(message)

suspend fun asyncPoll(
    /**
     * Function to retrieve payment session information periodically until it resolves or rejects.
     *
     * The function will be reinvoked in `pollInterval` until it times out.
     *
     * Rejections will stop the polling and will be propagated.
     */
    getPaymentSessionFn: suspend (sessionId: String) -> PaymentSession,

    /**
     * Session ID if the payment session.
     */
    sessionId: String,

    /**
     * Expected status of the payment session that results in a successful poll.
     */
    expectedStatus: PaymentSession.Status,

    /**
     * Array of status codes that indicate a failure state.
     */
    failureStatus: Array<PaymentSession.Status>,

    /**
     * Milliseconds interval between attempting to resolve the promise again.
     */
    pollInterval: Long,

    /**
     * Maximum time to continue polling to receive a successful resolved response.
     * If the promise doesn't resolve before the poll timeout then the poller
     * rejects with a timeout error.
     */
    pollTimeout: Long,
): PaymentSession {
    try {
        return withTimeout(pollTimeout) {
            while (isActive) {
                try {
                    val startTime = System.currentTimeMillis()
                    val paymentSession = getPaymentSessionFn(sessionId)
                    val elapsedTime = System.currentTimeMillis() - startTime
                    val status = paymentSession.status
                    if (status == expectedStatus) {
                        return@withTimeout paymentSession
                    } else if (failureStatus.contains(status)) {
                        throw SessionFailureStateException("Session failed with status: $status")
                    } else {
                        delay(pollInterval - elapsedTime)
                    }
                } catch (serverException: ServerException) {
                    delay(pollInterval)
                }
            }
            throw Exception("Unexpected exception")
        }
    } catch (timeoutException: TimeoutCancellationException) {
        throw PollTimeoutExeption("Poller reached timeout")
    }
}
