package com.idfinance.kmm.debug.view.presentation.component

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.idfinance.kmm.debug.view.ServiceLocator
import com.idfinance.kmm.debug.view.presentation.ui.LogView

fun openDebugView(context: Context) {
    context.startActivity(
        Intent(context, DebugViewActivity::class.java)
            .apply {
                flags += Intent.FLAG_ACTIVITY_NEW_TASK
            },
    )
}

class DebugViewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = ServiceLocator.getRootComponent(defaultComponentContext())
        setContent {
            LogView(root)
        }
    }
}
