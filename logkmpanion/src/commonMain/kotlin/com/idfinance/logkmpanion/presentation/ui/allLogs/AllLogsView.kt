package com.idfinance.logkmpanion.presentation.ui.allLogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.idfinance.logkmpanion.data.model.Log
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
internal fun AllLogsView(component: AllLogsComponent) {
    val model by component.model.subscribeAsState()
    val state = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(model.logs.size) {
        state.animateScrollToItem(maxOf(model.logs.size - 1, 0))
    }
    Scaffold(
        floatingActionButton = { FloatingActionButtons(component, snackbarHostState) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        LazyColumn(state = state, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(model.logs) { index, it ->
                Text(
                    getAttributedLog(it),
                    color = if (it.isError) MaterialTheme.colorScheme.error else LocalContentColor.current,
                    fontSize = TextUnit(12F, TextUnitType.Sp),
                )
                if (index < model.logs.lastIndex) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingActionButtons(component: AllLogsComponent, snackbarHostState: SnackbarHostState) {
    val model by component.model.subscribeAsState()
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        FloatingActionButton(onClick = component::clearLogs) {
            Icon(Icons.Default.Delete, null)
        }
        FloatingActionButton(onClick = {
            scope.launch {
                copyLogsAndNotify(
                    payload = model.clipboardPayload,
                    clipboardManager = clipboardManager,
                    snackbarHostState = snackbarHostState,
                    onShareConfirmed = component::shareFullLog,
                )
            }
        }) {
            Icon(Icons.Default.ContentCopy, null)
        }
    }
}

private suspend fun copyLogsAndNotify(
    payload: ClipboardPayload,
    clipboardManager: ClipboardManager,
    snackbarHostState: SnackbarHostState,
    onShareConfirmed: () -> Unit,
) {
    runCatching { clipboardManager.setText(AnnotatedString(payload.text)) }
    if (payload.wasTruncated) {
        val result = snackbarHostState.showSnackbar(
            message = "Copied last 512 KB. Share full log as file?",
            actionLabel = "Share",
            duration = SnackbarDuration.Long,
        )
        if (result == SnackbarResult.ActionPerformed) onShareConfirmed()
    } else {
        snackbarHostState.showSnackbar("Logs copied")
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun getAttributedLog(log: Log): AnnotatedString {
    val time = Instant.fromEpochMilliseconds(log.time)
        .toLocalDateTime(TimeZone.currentSystemDefault()).time
    val formattedTime = "${time.hour}:${time.minute}:${time.second}"
    val fullMessage = "[${log.tag}][$formattedTime] ${log.message}"
    val builder = AnnotatedString.Builder(fullMessage)
    val endTagIndex = fullMessage.indexOf(log.tag) + log.tag.length + 1
    builder.addStyle(SpanStyle(MaterialTheme.colorScheme.primary), 0, endTagIndex)
    return builder.toAnnotatedString()
}
