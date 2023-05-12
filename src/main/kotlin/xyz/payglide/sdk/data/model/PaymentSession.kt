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
     * Values: PAYMENT_INITIATED,PAYMENT_SUCCEEDED,PAYMENT_FAILED,TOKEN_TRANSFER_INITIATED,TOKEN_TRANSFER_COMPLETED
     */
    @Serializable
    enum class Status(val value: kotlin.String) {
        @SerialName(value = "PAYMENT_INITIATED")
        PAYMENT_INITIATED("PAYMENT_INITIATED"),

        @SerialName(value = "PAYMENT_SUCCEEDED")
        PAYMENT_SUCCEEDED("PAYMENT_SUCCEEDED"),

        @SerialName(value = "PAYMENT_FAILED")
        PAYMENT_FAILED("PAYMENT_FAILED"),

        @SerialName(value = "TOKEN_TRANSFER_INITIATED")
        TOKEN_TRANSFER_INITIATED("TOKEN_TRANSFER_INITIATED"),

        @SerialName(value = "TOKEN_TRANSFER_COMPLETED")
        TOKEN_TRANSFER_COMPLETED("TOKEN_TRANSFER_COMPLETED"),
        ;
    }
}
