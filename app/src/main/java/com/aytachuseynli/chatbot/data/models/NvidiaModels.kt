package com.aytachuseynli.chatbot.data.models

import com.google.gson.annotations.SerializedName

/**
 * NVIDIA API Request/Response DTOs
 * API Documentation: https://docs.api.nvidia.com/
 * Uses OpenAI-compatible chat completion endpoint
 */

data class NvidiaRequest(
    @SerializedName("model")
    val model: String = "meta/llama-3.1-8b-instruct",
    @SerializedName("messages")
    val messages: List<NvidiaMessage>,
    @SerializedName("max_tokens")
    val maxTokens: Int = 1024,
    @SerializedName("temperature")
    val temperature: Double = 0.7,
    @SerializedName("stream")
    val stream: Boolean = false
)

data class NvidiaMessage(
    @SerializedName("role")
    val role: String, // "user", "assistant", or "system"
    @SerializedName("content")
    val content: String
)

data class NvidiaResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("object")
    val objectType: String?,
    @SerializedName("created")
    val created: Long?,
    @SerializedName("model")
    val model: String?,
    @SerializedName("choices")
    val choices: List<NvidiaChoice>?,
    @SerializedName("usage")
    val usage: NvidiaUsage?
)

data class NvidiaChoice(
    @SerializedName("index")
    val index: Int?,
    @SerializedName("message")
    val message: NvidiaMessage?,
    @SerializedName("finish_reason")
    val finishReason: String?
)

data class NvidiaUsage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int?,
    @SerializedName("completion_tokens")
    val completionTokens: Int?,
    @SerializedName("total_tokens")
    val totalTokens: Int?
)
