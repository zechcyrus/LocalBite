package com.example.myfoodapp.auth

import com.example.myfoodapp.data.SupabaseClientProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.setBody
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthRepository {

    private val httpClient: HttpClient by lazy { HttpClient(Android) }

    suspend fun sendEmailOtp(email: String, createUserIfNeeded: Boolean): Result<Unit> {
        return runCatching {
            val url = "${SupabaseClientProvider.SUPABASE_URL}/auth/v1/otp"
            val payload = "{" +
                "\"email\":\"$email\"," +
                "\"create_user\":$createUserIfNeeded" +
                "}"
            val response = httpClient.post(url) {
                headers.append("apikey", SupabaseClientProvider.SUPABASE_ANON_KEY)
                headers.append(HttpHeaders.Authorization, "Bearer ${SupabaseClientProvider.SUPABASE_ANON_KEY}")
                contentType(ContentType.Application.Json)
                setBody(payload)
            }
            if (response.status != HttpStatusCode.OK && response.status != HttpStatusCode.NoContent) {
                throw IllegalStateException("OTP send failed: ${response.status} ${response.bodyAsText()}")
            }
        }
    }

    suspend fun verifyEmailOtp(email: String, otp: String): Result<Unit> {
        return runCatching {
            val url = "${SupabaseClientProvider.SUPABASE_URL}/auth/v1/verify"
            val payload = "{" +
                "\"type\":\"email\"," +
                "\"email\":\"$email\"," +
                "\"token\":\"$otp\"" +
                "}"
            val response = httpClient.post(url) {
                headers.append("apikey", SupabaseClientProvider.SUPABASE_ANON_KEY)
                headers.append(HttpHeaders.Authorization, "Bearer ${SupabaseClientProvider.SUPABASE_ANON_KEY}")
                contentType(ContentType.Application.Json)
                setBody(payload)
            }
            if (response.status != HttpStatusCode.OK && response.status != HttpStatusCode.Created) {
                throw IllegalStateException("OTP verify failed: ${response.status} ${response.bodyAsText()}")
            }
        }
    }

    suspend fun signOut(): Result<Unit> {
        return Result.success(Unit)
    }
}