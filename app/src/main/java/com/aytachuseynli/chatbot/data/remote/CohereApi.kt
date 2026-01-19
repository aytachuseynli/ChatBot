package com.aytachuseynli.chatbot.data.remote

import com.aytachuseynli.chatbot.data.models.CohereRequest
import com.aytachuseynli.chatbot.data.models.CohereResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Cohere API v2 Interface
 * Base URL: https://api.cohere.com/v2/
 * Documentation: https://docs.cohere.com/reference/chat
 *
 * Note: Using v2 API as v1 models were deprecated in September 2025.
 */
interface CohereApi {

    @POST("chat")
    suspend fun chat(
        @Header("Authorization") authorization: String,
        @Body request: CohereRequest
    ): CohereResponse

    companion object {
        const val BASE_URL = "https://api.cohere.com/v2/"
    }
}
