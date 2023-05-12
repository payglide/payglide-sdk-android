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
 * @param `value` The value of the argument
 * @param type The type of the argument
 */
@Serializable
data class Argument(

    /* The value of the argument */
    @SerialName(value = "value")
    val `value`: kotlin.String? = null,

    /* The type of the argument */
    @SerialName(value = "type")
    val type: kotlin.String? = null,

)
