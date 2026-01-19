# Multi-AI ChatBot

A modern Android chatbot application that sends user messages simultaneously to multiple AI providers (Mistral, NVIDIA, and Cohere) and displays their responses in real-time.

## Features

- **Multi-AI Integration**: Send messages to 3 AI providers simultaneously
- **Real-time Responses**: See responses as they arrive from each provider
- **Clean Architecture**: MVVM pattern with Clean Architecture layers
- **Modern UI**: Jetpack Compose with Material 3 Design
- **Dark/Light Theme**: Toggle between themes
- **Message History**: View conversation history
- **Copy to Clipboard**: Easily copy any AI response
- **Response Time**: See how long each AI took to respond
- **Markdown Support**: Bold text rendering
- **Scrollable Responses**: View full responses within cards

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Networking**: Retrofit 2.9.0 + OkHttp 4.12.0
- **Async**: Kotlin Coroutines + Flow

## Project Structure

```
app/src/main/java/com/aytachuseynli/chatbot/
├── data/
│   ├── models/          # API DTOs
│   ├── remote/          # Retrofit API interfaces
│   └── repository/      # Repository implementation
├── domain/
│   ├── model/           # Domain models
│   └── repository/      # Repository interface
├── presentation/
│   ├── components/      # UI components
│   ├── ChatScreen.kt    # Main screen
│   └── ChatViewModel.kt # ViewModel
├── di/                  # Hilt modules
└── ui/theme/            # App theming
```

## Setup Instructions

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 35+
- Min Android API 24 (Android 7.0)

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/ChatBot.git
cd ChatBot
```

### 2. Configure API Keys

**Important**: Never commit your API keys to version control!

1. Copy the example file:
   ```bash
   cp local.properties.example local.properties
   ```

2. Open `local.properties` and add your API keys:
   ```properties
   MISTRAL_API_KEY=your_mistral_key_here
   NVIDIA_API_KEY=your_nvidia_key_here
   COHERE_API_KEY=your_cohere_key_here
   ```

### 3. Get Your Free API Keys

#### Mistral AI
1. Visit [console.mistral.ai](https://console.mistral.ai/)
2. Create a free account
3. Go to API Keys section
4. Generate a new key

#### NVIDIA NIM
1. Visit [build.nvidia.com](https://build.nvidia.com/)
2. Sign up for free
3. Access the API section
4. Generate your API key

#### Cohere
1. Visit [dashboard.cohere.com](https://dashboard.cohere.com/)
2. Sign up for free trial
3. Navigate to API Keys
4. Copy your key

### 4. Build and Run

1. Open the project in Android Studio
2. Sync Gradle files
3. Run on emulator or device

## API Models Used

| Provider | Model | Description |
|----------|-------|-------------|
| Mistral | mistral-small-latest | Fast, efficient model |
| NVIDIA | meta/llama-3.1-8b-instruct | Llama 3.1 8B |
| Cohere | command-r7b-12-2024 | Command R7B chat model |

## Security Notes

- API keys are stored in `local.properties` which is gitignored
- Never commit sensitive data to version control
- The app uses HTTPS for all API communications
- ProGuard rules protect release builds

## Troubleshooting

### "API key not configured"
- Ensure keys are added to `local.properties`
- Rebuild the project after adding keys

### "Authentication failed"
- Verify API key is correct and active
- Check if you have API access enabled

### "Rate limit exceeded"
- Wait before sending another request
- Free tiers have limited requests

### Build errors
- Sync Gradle files
- Invalidate caches and restart Android Studio

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is for educational purposes.

## Acknowledgments

- [Mistral AI](https://mistral.ai/)
- [NVIDIA](https://nvidia.com/)
- [Cohere](https://cohere.com/)
