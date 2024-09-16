package com.example.aiconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.aiconnect.screens.chatscreen
import com.example.aiconnect.ui.theme.AIConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        setContent {
            AIConnectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    chatscreen(modifier = Modifier.padding(innerPadding),chatViewModel)
                }
            }
        }
    }
}
