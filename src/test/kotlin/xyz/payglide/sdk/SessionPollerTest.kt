package xyz.payglide.sdk

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import xyz.payglide.sdk.data.model.PaymentSession
import xyz.payglide.sdk.infrastructure.ClientException
import xyz.payglide.sdk.infrastructure.ServerException

typealias GetPaymentSession = suspend (sessionId: String) -> PaymentSession

@ExperimentalCoroutinesApi
internal class SessionPollerTest {
    private val POLL_INTERVAL = 1L
    private val POLL_TIMEOUT = 10L
    private val A_SESSION_ID = "a session ID"
    private val FAILURE_STATUS = PaymentSession.Status.pAYMENTFAILED
    private val COMPLETED_STATUS = PaymentSession.Status.tOKENTRANSFERCOMPLETED

    @Test
    fun `should return session immediately when status is COMPLETED_STATUS the first time`() = runTest {
        val mockGetPaymentSession = mockk<GetPaymentSession>(relaxed = true)
        coEvery { mockGetPaymentSession(A_SESSION_ID) } returns PaymentSession(status = COMPLETED_STATUS)

        val session = asyncPoll(
            getPaymentSessionFn = mockGetPaymentSession,
            sessionId = A_SESSION_ID,
            expectedStatus = COMPLETED_STATUS,
            failureStatus = arrayOf(FAILURE_STATUS),
            pollInterval = POLL_INTERVAL,
            pollTimeout = POLL_TIMEOUT,
        )
        coVerify(exactly = 1) { mockGetPaymentSession.invoke(A_SESSION_ID) }
        assertEquals(COMPLETED_STATUS, session.status)
    }

    @Test
    fun `should return session only when status is COMPLETED_STATUS`() = runTest {
        val mockGetPaymentSession = mockk<GetPaymentSession>(relaxed = true)
        coEvery { mockGetPaymentSession(A_SESSION_ID) } returnsMany listOf(
            PaymentSession(status = PaymentSession.Status.pAYMENTINITIATED),
            PaymentSession(status = PaymentSession.Status.pAYMENTSUCCEEDED),
            PaymentSession(status = PaymentSession.Status.tOKENTRANSFERINITIATED),
            PaymentSession(status = COMPLETED_STATUS),
        )

        val session = asyncPoll(
            getPaymentSessionFn = mockGetPaymentSession,
            sessionId = A_SESSION_ID,
            expectedStatus = COMPLETED_STATUS,
            failureStatus = arrayOf(FAILURE_STATUS),
            pollInterval = POLL_INTERVAL,
            pollTimeout = POLL_TIMEOUT,
        )
        coVerify(exactly = 4) { mockGetPaymentSession.invoke(A_SESSION_ID) }
        assertEquals(COMPLETED_STATUS, session.status)
    }

    @Test
    fun `should timeout when no COMPLETED_STATUS status returned`() = runTest {
        val mockGetPaymentSession = mockk<GetPaymentSession>(relaxed = true)
        coEvery { mockGetPaymentSession(A_SESSION_ID) } returns PaymentSession(status = PaymentSession.Status.pAYMENTINITIATED)
        try {
            val session = asyncPoll(
                getPaymentSessionFn = mockGetPaymentSession,
                sessionId = A_SESSION_ID,
                expectedStatus = COMPLETED_STATUS,
                failureStatus = arrayOf(FAILURE_STATUS),
                pollInterval = POLL_INTERVAL,
                pollTimeout = POLL_TIMEOUT,
            )
        } catch (exception: PollTimeoutExeption) {
            assertEquals("Poller reached timeout", exception.message)
            coVerify(atLeast = 1) { mockGetPaymentSession.invoke(A_SESSION_ID) }
        }
    }

    @Test
    fun `should fail with error when FAILURE_STATUS returned`() = runTest {
        val mockGetPaymentSession = mockk<GetPaymentSession>(relaxed = true)
        coEvery { mockGetPaymentSession(A_SESSION_ID) } returns PaymentSession(status = FAILURE_STATUS)
        try {
            val session = asyncPoll(
                getPaymentSessionFn = mockGetPaymentSession,
                sessionId = A_SESSION_ID,
                expectedStatus = COMPLETED_STATUS,
                failureStatus = arrayOf(FAILURE_STATUS),
                pollInterval = POLL_INTERVAL,
                pollTimeout = POLL_TIMEOUT,
            )
        } catch (exception: SessionFailureStateException) {
            assertEquals("Session failed with status: pAYMENTFAILED", exception.message)
            coVerify(atLeast = 1) { mockGetPaymentSession.invoke(A_SESSION_ID) }
        }
    }

    @Test
    fun `should fail with an error when the getSessionById fails`() = runTest {
        val mockGetPaymentSession = mockk<GetPaymentSession>(relaxed = true)
        coEvery { mockGetPaymentSession(A_SESSION_ID) }.throws(Exception("cannot get payment session"))
        try {
            val session = asyncPoll(
                getPaymentSessionFn = mockGetPaymentSession,
                sessionId = A_SESSION_ID,
                expectedStatus = COMPLETED_STATUS,
                failureStatus = arrayOf(FAILURE_STATUS),
                pollInterval = POLL_INTERVAL,
                pollTimeout = POLL_TIMEOUT,
            )
        } catch (exception: Exception) {
            assertEquals("cannot get payment session", exception.message)
            coVerify(atLeast = 1) { mockGetPaymentSession.invoke(A_SESSION_ID) }
        }
    }

    @Test
    fun `should fail with an error when ClientException`() = runTest {
        val mockGetPaymentSession = mockk<GetPaymentSession>(relaxed = true)
        coEvery { mockGetPaymentSession(A_SESSION_ID) }.throws(ClientException("cannot get payment session"))
        try {
            val session = asyncPoll(
                getPaymentSessionFn = mockGetPaymentSession,
                sessionId = A_SESSION_ID,
                expectedStatus = COMPLETED_STATUS,
                failureStatus = arrayOf(FAILURE_STATUS),
                pollInterval = POLL_INTERVAL,
                pollTimeout = POLL_TIMEOUT,
            )
        } catch (exception: ClientException) {
            assertEquals("cannot get payment session", exception.message)
            coVerify(atLeast = 1) { mockGetPaymentSession.invoke(A_SESSION_ID) }
        }
    }

    @Test
    fun `should retry until timeout when ServerException`() = runTest {
        val mockGetPaymentSession = mockk<GetPaymentSession>(relaxed = true)
        coEvery { mockGetPaymentSession(A_SESSION_ID) }.throws(ServerException("cannot get payment session"))
        try {
            val session = asyncPoll(
                getPaymentSessionFn = mockGetPaymentSession,
                sessionId = A_SESSION_ID,
                expectedStatus = COMPLETED_STATUS,
                failureStatus = arrayOf(FAILURE_STATUS),
                pollInterval = POLL_INTERVAL,
                pollTimeout = POLL_TIMEOUT,
            )
        } catch (exception: PollTimeoutExeption) {
            assertEquals("Poller reached timeout", exception.message)
            coVerify(atLeast = 1) { mockGetPaymentSession.invoke(A_SESSION_ID) }
        }
    }
}
