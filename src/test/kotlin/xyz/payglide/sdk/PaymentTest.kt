package xyz.payglide.sdk

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import xyz.payglide.sdk.data.api.PaymentApi
import xyz.payglide.sdk.data.model.PaymentSession

internal class PaymentTest {

    @Test
    fun `should initialise with correct payment session data`() {
        val A_SESSION_ID = "A_SESSION_ID"
        val AN_AMOUNT = "42"
        val A_CURRENCY = "USDC"
        val AN_ADDRESS = "0x123"
        val A_HASH = "hash"
        val validSession = PaymentSession(
            id = A_SESSION_ID,
            amount = AN_AMOUNT,
            currency = A_CURRENCY,
            address = AN_ADDRESS,
            status = PaymentSession.Status.tOKENTRANSFERCOMPLETED,
            transactionHash = A_HASH,
        )

        val payment = Payment(validSession, 1, 10, PaymentApi())

        assertEquals(A_SESSION_ID, payment.sessionId)
        assertEquals(AN_AMOUNT, payment.amount)
        assertEquals(A_CURRENCY, payment.currency)
        assertEquals(AN_ADDRESS, payment.address)
        assertEquals(A_HASH, payment.transactionHash)
        assertEquals("https://test.payglide.io/wallet-pay?sessionId=$A_SESSION_ID", payment.getPaymentPage())
    }

    @Test
    fun `should fail with error if invalid session provided`() {
        val invalidSession = PaymentSession(
            id = null,
            amount = null,
            currency = null,
            address = null,
            status = null,
            transactionHash = null,
        )
        assertThrows<PaymentSessionException> {
            val payment = Payment(invalidSession, 1, 10, PaymentApi())
        }
    }
}
