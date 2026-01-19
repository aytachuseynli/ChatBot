package com.aytachuseynli.chatbot.data.models

import com.google.gson.annotations.SerializedName

/**
 * Mistral API Request/Response DTOs
 * API Documentation: https://docs.mistral.ai/api/
 */

data class MistralRequest(
    @SerializedName("model")
    val model: String = "mistral-small-latest",
    @SerializedName("messages")
    val messages: List<MistralMessage>,
    @SerializedName("max_tokens")
    val maxTokens: Int = 1024,
    @SerializedName("temperature")
    val temperature: Double = 0.7
)

data class MistralMessage(
    @SerializedName("role")
    val role: String, // "user" or "assistant"
    @SerializedName("content")
    val content: String
)

data class MistralResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("object")
    val objectType: String?,
    @SerializedName("created")
    val created: Long?,
    @SerializedName("model")
    val model: String?,
    @SerializedName("choices")
    val choices: List<MistralChoice>?,
    @SerializedName("usage")
    val usage: MistralUsage?
)

data class MistralChoice(
    @SerializedName("index")
    val index: Int?,
    @SerializedName("message")
    val message: MistralMessage?,
    @SerializedName("finish_reason")
    val finishReason: String?
)

data class MistralUsage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int?,
    @SerializedName("completion_tokens")
    val completionTokens: Int?,
    @SerializedName("total_tokens")
    val totalTokens: Int?
)
