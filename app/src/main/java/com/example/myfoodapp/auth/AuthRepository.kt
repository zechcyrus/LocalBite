package com.example.myfoodapp.auth

import com.example.myfoodapp.data.SupabaseClientProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class AuthRepository {

    private val httpClient: HttpClient by lazy { HttpClient(Android) }
    private val json: Json by lazy { Json { ignoreUnknownKeys = true } }

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

    suspend fun verifyEmailOtp(email: String, otp: String): Result<AuthSession> {
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
            val body = response.bodyAsText()
            if (response.status != HttpStatusCode.OK && response.status != HttpStatusCode.Created) {
                throw IllegalStateException("OTP verify failed: ${response.status} $body")
            }
            val sessionDto = json.decodeFromString(SupabaseVerifyResponse.serializer(), body)
            val userId = sessionDto.user.id
            val session = AuthSession(
                accessToken = sessionDto.accessToken,
                refreshToken = sessionDto.refreshToken,
                userId = userId,
                email = sessionDto.user.email
            )
            session
        }
    }

    suspend fun upsertUserProfile(
        session: AuthSession,
        input: UserProfileInput
    ): Result<Unit> {
        return runCatching {
            val url = "${SupabaseClientProvider.SUPABASE_URL}/rest/v1/user_profiles?on_conflict=id"
            val payload = "{" +
                "\"id\":\"${session.userId}\"," +
                "\"email\":\"${input.email}\"," +
                (input.name?.let { "\"name\":\"$it\"," } ?: "") +
                (input.phone?.let { "\"phone\":\"$it\"," } ?: "") +
                (input.address?.let { "\"address\":\"$it\"," } ?: "") +
                (input.userType?.let { "\"user_type\":\"$it\"" } ?: "") +
                "}"
            val response = httpClient.post(url) {
                headers.append("apikey", SupabaseClientProvider.SUPABASE_ANON_KEY)
                headers.append(HttpHeaders.Authorization, "Bearer ${session.accessToken}")
                headers.append("Prefer", "resolution=merge-duplicates,return=representation")
                contentType(ContentType.Application.Json)
                setBody(payload)
            }
            if (response.status != HttpStatusCode.Created && response.status != HttpStatusCode.OK) {
                throw IllegalStateException("Profile upsert failed: ${response.status} ${response.bodyAsText()}")
            }
        }
    }

    suspend fun signOut(): Result<Unit> {
        return Result.success(Unit)
    }
}

@Serializable
data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val email: String
)

@Serializable
data class UserProfileInput(
    val email: String,
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val userType: String? = null
)

@Serializable
private data class SupabaseVerifyResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    val user: SupabaseUser
)

@Serializable
private data class SupabaseUser(
    val id: String,
    val email: String
)