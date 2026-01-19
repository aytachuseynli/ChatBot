package com.aytachuseynli.chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.aytachuseynli.chatbot.presentation.ChatScreen
import com.aytachuseynli.chatbot.presentation.ChatViewModel
import com.aytachuseynli.chatbot.ui.theme.ChatBotTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity for the ChatBot application.
 * Annotated with @AndroidEntryPoint to enable Hilt dependency injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: ChatViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            ChatBotTheme(darkTheme = uiState.isDarkTheme) {
                ChatScreen(viewModel = viewModel)
            }
        }
    }
}
