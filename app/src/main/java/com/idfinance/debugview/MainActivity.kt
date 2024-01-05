package com.idfinance.debugview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.lifecycle.lifecycleScope
import com.idfinance.kmm.debug.view.data.network.debugViewNetworkPlugin
import com.idfinance.kmm.debug.view.domain.LogType
import com.idfinance.kmm.debug.view.domain.handleLog
import com.idfinance.kmm.debug.view.presentation.component.openDebugView
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val httpClient by lazy {
        HttpClient { install(debugViewNetworkPlugin()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                Button(onClick = {
                    repeat(100) { handleLog(LogType.DEFAULT, "TAG", "message$it") }
                    openDebugView(applicationContext)
                }) {
                    Text("Open debug view")
                }
                Button(onClick = {
                    lifecycleScope.launch(CoroutineExceptionHandler { _, throwable ->
                        throwable.printStackTrace()
                    }) {
                        httpClient.get("https://api64.ipify.org?format=json")
                    }
                }) {
                    Text("Fake api call")
                }
            }
        }
    }
}