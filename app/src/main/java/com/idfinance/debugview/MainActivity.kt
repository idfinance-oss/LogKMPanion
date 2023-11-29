package com.idfinance.debugview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.idfinance.kmm.debug.view.domain.LogType
import com.idfinance.kmm.debug.view.domain.handleLog
import com.idfinance.kmm.debug.view.presentation.component.openDebugView

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