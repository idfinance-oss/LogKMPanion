package com.idfinance.debugview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.idfinance.kmm.debugview.domain.LogType
import com.idfinance.kmm.debugview.domain.handleLog
import com.idfinance.kmm.debugview.presentation.component.openDebugView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Button(onClick = {
                repeat(100) { handleLog(LogType.DEFAULT, "TAG", "message$it") }
                openDebugView(applicationContext)
            }) {
                Text("Open debug view")
            }
        }
    }
}