package com.aytachuseynli.chatbot.data.remote

import com.aytachuseynli.chatbot.data.models.NvidiaRequest
import com.aytachuseynli.chatbot.data.models.NvidiaResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * NVIDIA API Interface
 * Base URL: https://integrate.api.nvidia.com/v1/
 * Documentation: https://docs.api.nvidia.com/
 *
 * NVIDIA provides OpenAI-compatible endpoints with free tier access
 * to various models including Llama, Mixtral, and more.
 */
interface NvidiaApi {

    @POST("chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: NvidiaRequest
    ): NvidiaResponse

    companion object {
        const val BASE_URL = "https://integrate.api.nvidia.com/v1/"
    }
}
