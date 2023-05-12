
# PaymentSession

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **kotlin.String** | Payment session ID |  [optional]
**amount** | **kotlin.String** | The amount of cryptocurrency required to run the transaction |  [optional]
**currency** | **kotlin.String** | The cryptocurrency used for the transaction |  [optional]
**address** | **kotlin.String** | The wallet address used to execute the transaction |  [optional]
**status** | [**inline**](#Status) | Payment Status |  [optional]
**transactionHash** | **kotlin.String** | Hash of the blockchain transaction the session was initialised with |  [optional]


<a name="Status"></a>
## Enum: status
Name | Value
---- | -----
status | PAYMENT_INITIATED, PAYMENT_SUCCEEDED, PAYMENT_FAILED, TOKEN_TRANSFER_INITIATED, TOKEN_TRANSFER_COMPLETED



