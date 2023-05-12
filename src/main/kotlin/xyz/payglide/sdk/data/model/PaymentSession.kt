@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport",
)

package xyz.payglide.sdk.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * *
 * @param id Payment session ID
 * @param amount The amount of cryptocurrency required to run the transaction
 * @param currency The cryptocurrency used for the transaction
 * @param address The wallet address used to execute the transaction
 * @param status Payment Status
 * @param transactionHash Hash of the blockchain transaction the session was initialised with
 */
@Serializable
data class PaymentSession(

    /* Payment session ID */
    @SerialName(value = "id")
    val id: kotlin.String? = null,

    /* The amount of cryptocurrency required to run the transaction */
    @SerialName(value = "amount")
    val amount: kotlin.String? = null,

    /* The cryptocurrency used for the transaction */
    @SerialName(value = "currency")
    val currency: kotlin.String? = null,

    /* The wallet address used to execute the transaction */
    @SerialName(value = "address")
    val address: kotlin.String? = null,

    /* Payment Status */
    @SerialName(value = "status")
    val status: PaymentSession.Status? = null,

    /* Hash of the blockchain transaction the session was initialised with */
    @SerialName(value = "transactionHash")
    val transactionHash: kotlin.String? = null,

) {

    /**
     * Payment Status
     *
     * Values: pAYMENTINITIATED,pAYMENTSUCCEEDED,pAYMENTFAILED,tOKENTRANSFERINITIATED,tOKENTRANSFERCOMPLETED
     */
    @Serializable
    enum class Status(val value: kotlin.String) {
        @SerialName(value = "PAYMENT_INITIATED")
        pAYMENTINITIATED("PAYMENT_INITIATED"),

        @SerialName(value = "PAYMENT_SUCCEEDED")
        pAYMENTSUCCEEDED("PAYMENT_SUCCEEDED"),

        @SerialName(value = "PAYMENT_FAILED")
        pAYMENTFAILED("PAYMENT_FAILED"),

        @SerialName(value = "TOKEN_TRANSFER_INITIATED")
        tOKENTRANSFERINITIATED("TOKEN_TRANSFER_INITIATED"),

        @SerialName(value = "TOKEN_TRANSFER_COMPLETED")
        tOKENTRANSFERCOMPLETED("TOKEN_TRANSFER_COMPLETED"),
;
    }
}
