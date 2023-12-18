package com.idfinance.kmm.debug.view.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.idfinance.kmm.debug.view.data.model.Log
import com.idfinance.kmm.debug.view.presentation.decompose.DebugComponent
import com.idfinance.kmm.debug.view.presentation.ui.theme.DebugViewTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
internal fun LogView(component: DebugComponent) {
    val model by component.model.subscribeAsState()
    val state = rememberLazyListState()

    LaunchedEffect(model.logs.size) {
        state.animateScrollToItem(maxOf(model.logs.size - 1, 0))
    }

    DebugViewTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Scaffold(
                floatingActionButton = {
                    val clipboardManager = LocalClipboardManager.current
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        FloatingActionButton(onClick = component::close) {
                            Image(Icons.Default.ArrowBack, null)
                        }
                        FloatingActionButton(onClick = component::clearLogs) {
                            Image(Icons.Default.Delete, null)
                        }
                        FloatingActionButton(onClick = { clipboardManager.setText(AnnotatedString(model.concatenatedLog)) }) {
                            Image(Icons.Default.ContentCopy, null)
                        }
                    }
                },
            ) {
                LazyColumn(state = state, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    itemsIndexed(model.logs) { index, it ->
                        Text(
                            getAttributedLog(it),
                            color = if (it.isError) Color.Red else Color.Black,
                            fontSize = TextUnit(12F, TextUnitType.Sp),
                        )
                        if (index < model.logs.lastIndex) {
                            Divider(color = Color.Black, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

private fun getAttributedLog(log: Log): AnnotatedString {
    val time = Instant.fromEpochMilliseconds(log.time).toLocalDateTime(TimeZone.currentSystemDefault()).time
    val formattedTime = "${time.hour}:${time.minute}:${time.second}"
    val fullMessage = "[${log.tag}][$formattedTime] ${log.message}"
    val builder = AnnotatedString.Builder(fullMessage)
    val endTagIndex = fullMessage.indexOf(log.tag) + log.tag.length + 1
    builder.addStyle(SpanStyle(Color.Blue), 0, endTagIndex)
    return builder.toAnnotatedString()
}
