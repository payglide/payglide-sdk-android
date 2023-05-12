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
 * @param address The wallet address used to execute the transaction
 * @param code The blockchain transaction code
 * @param arguments The blockchain transaction arguments
 */
@Serializable
data class InitPaymentRequest(

    /* The wallet address used to execute the transaction */
    @SerialName(value = "address")
    val address: kotlin.String? = null,

    /* The blockchain transaction code */
    @SerialName(value = "code")
    val code: kotlin.String? = null,

    /* The blockchain transaction arguments */
    @SerialName(value = "arguments")
    val arguments: kotlin.collections.List<Argument>? = null,

)
