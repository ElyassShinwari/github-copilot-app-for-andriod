# GitHub Copilot Android App

An Android application that allows you to interact with GitHub Copilot's chat API directly from your mobile device.

## Features

- Chat interface to interact with GitHub Copilot
- Send coding questions and get AI-powered responses
- Clean and simple Material Design UI
- Secure API token input

## Setup

1. **Prerequisites:**
   - Android Studio (latest version)
   - Android SDK with API level 24+
   - A GitHub Copilot subscription

2. **Get GitHub Copilot API Access:**
   - You need access to GitHub Copilot API
   - Generate an API token from GitHub settings
   - Note: GitHub Copilot API access may require specific permissions

3. **Build the App:**
   ```bash
   # Open the project in Android Studio
   # Or build from command line:
   ./gradlew assembleDebug
   ```

4. **Install on Device:**
   ```bash
   ./gradlew installDebug
   ```

## Usage

1. Launch the app
2. Enter your GitHub Copilot API token
3. Type your question or coding prompt
4. Tap "Send to Copilot"
5. View the AI-generated response

## Important Notes

- Keep your API token secure and never share it
- The app requires an active internet connection
- GitHub Copilot API usage may be subject to rate limits
- You need an active GitHub Copilot subscription

## Technical Stack

- **Language:** Kotlin
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34
- **Key Libraries:**
  - OkHttp for HTTP requests
  - Gson for JSON parsing
  - Material Design Components

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/copilotapp/
│   │   └── MainActivity.kt
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml
│   │   └── values/
│   │       └── strings.xml
│   └── AndroidManifest.xml
└── build.gradle
```

## Permissions

- `INTERNET` - Required for API communication

## License

This is a sample project for educational purposes.
