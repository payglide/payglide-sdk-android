package xyz.payglide.sdk.infrastructure

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URI
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.Date
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

object Serializer {
    @JvmStatic
    val kotlinSerializationAdapters = SerializersModule {
        contextual(BigDecimal::class, BigDecimalAdapter)
        contextual(BigInteger::class, BigIntegerAdapter)
        contextual(Date::class, DateAdapter)
        contextual(LocalDate::class, LocalDateAdapter)
        contextual(LocalDateTime::class, LocalDateTimeAdapter)
        contextual(OffsetDateTime::class, OffsetDateTimeAdapter)
        contextual(UUID::class, UUIDAdapter)
        contextual(AtomicInteger::class, AtomicIntegerAdapter)
        contextual(AtomicLong::class, AtomicLongAdapter)
        contextual(AtomicBoolean::class, AtomicBooleanAdapter)
        contextual(URI::class, URIAdapter)
        contextual(URL::class, URLAdapter)
        contextual(StringBuilder::class, StringBuilderAdapter)
    }

    @JvmStatic
    val jvmJson: Json by lazy { Json { serializersModule = kotlinSerializationAdapters } }
}
