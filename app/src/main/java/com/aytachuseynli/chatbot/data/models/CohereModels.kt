package com.aytachuseynli.chatbot.data.models

import com.google.gson.annotations.SerializedName

/**
 * Cohere API v2 Request/Response DTOs
 * API Documentation: https://docs.cohere.com/reference/chat
 *
 * Note: Cohere v1 chat endpoint and models (command-r, command-r-plus)
 * were deprecated in September 2025. Using v2 API with current models.
 */

data class CohereRequest(
    @SerializedName("model")
    val model: String = "command-r7b-12-2024",
    @SerializedName("messages")
    val messages: List<CohereMessage>,
    @SerializedName("max_tokens")
    val maxTokens: Int = 1024,
    @SerializedName("temperature")
    val temperature: Double = 0.7
)

data class CohereMessage(
    @SerializedName("role")
    val role: String, // "user" or "assistant"
    @SerializedName("content")
    val content: String
)

data class CohereResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("message")
    val message: CohereResponseMessage?,
    @SerializedName("finish_reason")
    val finishReason: String?,
    @SerializedName("usage")
    val usage: CohereUsage?
)

data class CohereResponseMessage(
    @SerializedName("role")
    val role: String?,
    @SerializedName("content")
    val content: List<CohereContent>?
)

data class CohereContent(
    @SerializedName("type")
    val type: String?,
    @SerializedName("text")
    val text: String?
)

data class CohereUsage(
    @SerializedName("billed_units")
    val billedUnits: CohereBilledUnits?,
    @SerializedName("tokens")
    val tokens: CohereTokens?
)

data class CohereBilledUnits(
    @SerializedName("input_tokens")
    val inputTokens: Int?,
    @SerializedName("output_tokens")
    val outputTokens: Int?
)

data class CohereTokens(
    @SerializedName("input_tokens")
    val inputTokens: Int?,
    @SerializedName("output_tokens")
    val outputTokens: Int?
)
