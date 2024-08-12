package com.idfinance.logkmpanion.presentation.component

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.idfinance.logkmpanion.presentation.ui.root.RootView
import com.idfinance.logkmpanion.ServiceLocator

fun openLogKMPanion(context: Context) {
    context.startActivity(
        Intent(context, LogKMPanionActivity::class.java)
            .apply {
                flags += Intent.FLAG_ACTIVITY_NEW_TASK
            },
    )
}

class LogKMPanionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = ServiceLocator.getRootComponent(defaultComponentContext(), this::finish)
        setContent {
            RootView(root)
        }
    }
}
