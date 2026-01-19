package com.aytachuseynli.chatbot.data.util

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Extension function to convert throwable to user-friendly error message.
 */
fun Throwable.toUserFriendlyMessage(): String = when (this) {
    is HttpException -> when (code()) {
        401 -> "Authentication failed. Check API key."
        403 -> "Access forbidden. Check API permissions."
        404 -> "API endpoint not found. Model may be deprecated."
        429 -> "Rate limit exceeded. Please wait."
        in 500..599 -> "Server error. Try again later."
        else -> "HTTP error: ${code()}"
    }
    is SocketTimeoutException -> "Request timed out."
    is UnknownHostException -> "No internet connection."
    is IOException -> "Network error. Check your connection."
    else -> message ?: "Unknown error occurred"
}
