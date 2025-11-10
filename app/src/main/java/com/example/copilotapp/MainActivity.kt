package com.example.copilotapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {
    
    private lateinit var apiKeyInput: EditText
    private lateinit var promptInput: EditText
    private lateinit var sendButton: Button
    private lateinit var responseText: TextView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var scrollView: ScrollView
    
    private val client = OkHttpClient()
    private val gson = Gson()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        apiKeyInput = findViewById(R.id.apiKeyInput)
        promptInput = findViewById(R.id.promptInput)
        sendButton = findViewById(R.id.sendButton)
        responseText = findViewById(R.id.responseText)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        scrollView = findViewById(R.id.scrollView)
        
        sendButton.setOnClickListener {
            sendPromptToCopilot()
        }
    }
    
    private fun sendPromptToCopilot() {
        val apiKey = apiKeyInput.text.toString().trim()
        val prompt = promptInput.text.toString().trim()
        
        if (apiKey.isEmpty()) {
            Toast.makeText(this, "Please enter GitHub Token", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (prompt.isEmpty()) {
            Toast.makeText(this, "Please enter a prompt", Toast.LENGTH_SHORT).show()
            return
        }
        
        loadingIndicator.visibility = View.VISIBLE
        sendButton.isEnabled = false
        responseText.text = "Waiting for response..."
        
        val requestBody = mapOf(
            "messages" to listOf(
                mapOf(
                    "role" to "user",
                    "content" to prompt
                )
            ),
            "model" to "gpt-4o",
            "max_tokens" to 2000,
            "temperature" to 0.7
        )
        
        val json = gson.toJson(requestBody)
        val body = json.toRequestBody("application/json".toMediaType())
        
        val request = Request.Builder()
            .url("https://api.githubcopilot.com/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()
        
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    loadingIndicator.visibility = View.GONE
                    sendButton.isEnabled = true
                    responseText.text = "Error: ${e.message}\n\nNote: You need a GitHub Copilot API token. Get it from:\n1. GitHub Settings > Developer settings > Personal access tokens\n2. Or use GitHub Copilot API endpoint with proper authentication"
                }
            }
            
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                runOnUiThread {
                    loadingIndicator.visibility = View.GONE
                    sendButton.isEnabled = true
                    
                    if (response.isSuccessful && responseBody != null) {
                        try {
                            val jsonResponse = gson.fromJson(responseBody, Map::class.java)
                            val choices = jsonResponse["choices"] as? List<*>
                            val message = (choices?.get(0) as? Map<*, *>)?.get("message") as? Map<*, *>
                            val content = message?.get("content") as? String
                            
                            responseText.text = content ?: "No response content"
                            scrollView.post {
                                scrollView.fullScroll(View.FOCUS_DOWN)
                            }
                        } catch (e: Exception) {
                            responseText.text = "Response: $responseBody"
                        }
                    } else {
                        responseText.text = "Error ${response.code}: $responseBody\n\nMake sure you're using a valid GitHub Copilot API token."
                    }
                }
            }
        })
    }
}
