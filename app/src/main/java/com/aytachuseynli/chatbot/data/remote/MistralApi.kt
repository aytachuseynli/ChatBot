package com.aytachuseynli.chatbot.data.remote

import com.aytachuseynli.chatbot.data.models.MistralRequest
import com.aytachuseynli.chatbot.data.models.MistralResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Mistral AI API Interface
 * Base URL: https://api.mistral.ai/v1/
 * Documentation: https://docs.mistral.ai/api/
 */
interface MistralApi {

    @POST("chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: MistralRequest
    ): MistralResponse

    companion object {
        const val BASE_URL = "https://api.mistral.ai/v1/"
    }
}
