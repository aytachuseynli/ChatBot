package com.aytachuseynli.chatbot.presentation.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/**
 * Parses basic markdown formatting (bold, italic) and returns an AnnotatedString
 */
fun parseMarkdownText(text: String): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0
        val boldPattern = Regex("\\*\\*(.+?)\\*\\*")
        val italicPattern = Regex("\\*(.+?)\\*")

        // Combined pattern for bold and italic
        val combinedPattern = Regex("\\*\\*(.+?)\\*\\*|\\*(.+?)\\*|__(.+?)__|_(.+?)_")

        val matches = combinedPattern.findAll(text).toList()

        if (matches.isEmpty()) {
            append(text)
            return@buildAnnotatedString
        }

        for (match in matches) {
            // Append text before the match
            if (match.range.first > currentIndex) {
                append(text.substring(currentIndex, match.range.first))
            }

            val matchedText = match.value
            when {
                // Bold: **text** or __text__
                matchedText.startsWith("**") && matchedText.endsWith("**") -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(matchedText.removeSurrounding("**"))
                    }
                }
                matchedText.startsWith("__") && matchedText.endsWith("__") -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(matchedText.removeSurrounding("__"))
                    }
                }
                // Italic: *text* or _text_
                matchedText.startsWith("*") && matchedText.endsWith("*") -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(matchedText.removeSurrounding("*"))
                    }
                }
                matchedText.startsWith("_") && matchedText.endsWith("_") -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(matchedText.removeSurrounding("_"))
                    }
                }
                else -> {
                    append(matchedText)
                }
            }

            currentIndex = match.range.last + 1
        }

        // Append remaining text after last match
        if (currentIndex < text.length) {
            append(text.substring(currentIndex))
        }
    }
}