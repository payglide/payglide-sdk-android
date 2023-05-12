<div align="center">
	<h1>PayGlide SDK for Android</h1>
</div>

[![](https://img.shields.io/badge/license-APACHE2-blue.svg)](https://github.com/awslabs/aws-sdk-kotlin/blob/main/LICENSE)
[![](https://jitpack.io/v/payglide/payglide-sdk-android.svg)](https://jitpack.io/#payglide/payglide-sdk-android)

## Installation

To get payglide-sdk-android project into your build:

Step 1.
Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.payglide:payglide-sdk-android:0.1.1'
}
```
## Usage

Import and initialize a client using an **api key**

```kotlin
import xyz.payglide.sdk.PayGlideClient
import xyz.payglide.sdk.ClientConfig
import xyz.payglide.sdk.data.model.Argument
import xyz.payglide.sdk.data.model.InitPaymentRequest

// Initializing a client
val client = PayGlideClient(ClientConfig(apiKey= "your api key"))
```

### **initPaymentSession**
> PaymentSession initPaymentSession(initPaymentRequest)

Create a new payment session based on the transaction data


```kotlin
val paymentSession = client.initPaymentSession(
    InitPaymentRequest(
        address = "0x6f9ce307c114f443",
        code = "test script",
        arguments = listOf(
            Argument(
                value = "3.1",
                type = "UFix64",
            ),
        ),
    )
)
```

The initPaymentSession function returns a PaymentSession response.

Example response in json format:
```json
{
  "currency": "USDC",
  "status": "TOKEN_TRANSFER_COMPLETED",
  "amount": "3.1",
  "address": "0x6f9ce307c114f443",
  "id": "23999587-b312-4e1f-950e-5e95984fea44",
  "transactionHash": "cea6b2d18abce452e0b57a7393642a9358de23ebd5dffa1589563162d0ab44c4"
}
```

### **getSessionById**
> PaymentSession getSessionById(sessionId)

Find session by ID

Returns the session information

```kotlin
val sessionId : kotlin.String = sessionId_example // kotlin.String | ID of session to return
try {
    val result : PaymentSession = client.getSessionById(sessionId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling PayGlideClient#getSessionById")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling PayGlideClient#getSessionById")
    e.printStackTrace()
}
```

### **initPayment**
> Payment initPayment(initPaymentRequest)

The initPaynent method returns the Payment object.
Payment is a helper class for polling the payment session status and provides with other convenience methods.

```kotlin
val payment = client.initPayment(
    InitPaymentRequest(
        address = "0x6f9ce307c114f443",
        code = "test script",
        arguments = listOf(
            Argument(
                value = "3.1",
                type = "UFix64",
            ),
        ),
    )
)
```

### **Payment object with convenience methods**
> getPaymentPage()

Convenience method to get the url for the payment page where users can complete their payment.
```kotlin
val paymentPageUrl = payment.getPaymentPage()
```


> getCurrentStatus()

Convenience method to retrieve the current payment status associated with the payment.
```kotlin
val currentStatus = payment.getCurrentStatus()
```
Posible payment status:

- PAYMENT_INITIATED
- PAYMENT_SUCCEEDED
- PAYMENT_FAILED
- TOKEN_TRANSFER_INITIATED
- TOKEN_TRANSFER_COMPLETED


> PaymentSession onTransactionComplete()

Convenience method to poll the payment status and resolve the promise when the payment is complete.
```kotlin
val finalSession = payment.onTransactionComplete()
```

### PayGlideClient configuration

The `PayGlideClient` supports the following configuration on initialization. These options are all keys in the ClientConfig object set in the `config` constructor parameter.

| Option        | Default value            | Type         | Description                                                                                                                                                  |
| ------------- | ------------------------ | ------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `apiKey`      | `null`              |   `string`   | API Key provided to individual projects.                                                      |
| `pollInterval`| `5000 (5 seconds)`       |   `number`   | Milliseconds interval between attempting to resolve the promise again.                                                 |
| `pollTimeout` | `300000 (5 minutes)`     |   `number`   | Maximum time in milliseconds to continue polling to receive a successful resolved response. If the promise doesn't resolve before the poll timeout then the poller rejects with a timeout error.                                                 |

<a name="documentation-for-models"></a>
### Documentation for Models

- [xyz.payglide.sdk.data.model.Argument](docs/Argument.md)
- [xyz.payglide.sdk.data.model.InitPaymentRequest](docs/InitPaymentRequest.md)
- [xyz.payglide.sdk.data.model.PaymentSession](docs/PaymentSession.md)

## Requires

* Kotlin 1.4.30
* Gradle 6.8.3

## LICENSE

[Apache-2.0](LICENSE)
