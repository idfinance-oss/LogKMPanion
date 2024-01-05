package com.idfinance.kmm.debug.view.presentation.ui.networkLogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.idfinance.kmm.debug.view.presentation.ui.networkLogs.details.NetworkLogDetailsComponent
import com.idfinance.kmm.debug.view.presentation.ui.networkLogs.details.NetworkLogDetailsView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NetworkLogsView(component: NetworkLogsComponent) {
    val model by component.model.subscribeAsState()
    val modal by component.modal.subscribeAsState()
    val listState = rememberLazyListState()
    val bottomSheetState = rememberModalBottomSheetState(true)

    LaunchedEffect(model.logs.size) {
        listState.animateScrollToItem(maxOf(model.logs.size - 1, 0))
    }

    Scaffold(floatingActionButton = { FloatingActionButtons(component) }) {
        LazyColumn(state = listState) {
            model.logs.forEach { groupedLogs ->
                item {
                    Text(
                        "Session started at ${groupedLogs.sessionStartTime}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = TextUnit(12F, TextUnitType.Sp)
                    )
                }
                itemsIndexed(groupedLogs.logs) { index, it ->
                    Column {
                        Row(
                            modifier = Modifier
                                .clickable { component.openRequestDetails(it._id) }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                it.request?.method ?: "",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = TextUnit(12F, TextUnitType.Sp),
                            )
                            Text(
                                it.request?.url ?: "",
                                modifier = Modifier.padding(horizontal = 16.dp)
                                    .weight(1F),
                                fontSize = TextUnit(12F, TextUnitType.Sp),
                            )

                            if (it.response != null) {
                                Text(
                                    it.response?.statusCode.toString(),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = TextUnit(12F, TextUnitType.Sp),
                                )
                            } else if (it.error != null) {
                                Text(
                                    "Error",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = TextUnit(12F, TextUnitType.Sp),
                                )
                            } else {
                                CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 2.dp)
                            }
                        }

                        if (index < groupedLogs.logs.lastIndex) {
                            Divider(
                                color = MaterialTheme.colorScheme.outlineVariant,
                                thickness = 1.dp,
                            )
                        }
                    }
                }
                item { Box(modifier = Modifier.height(20.dp)) }
            }
        }

        val networkLogDetailsComponent = modal.child?.instance as? NetworkLogDetailsComponent
        networkLogDetailsComponent?.let {
            ModalBottomSheet(networkLogDetailsComponent::onDismiss, sheetState = bottomSheetState) {
                NetworkLogDetailsView(networkLogDetailsComponent)
            }
        }
    }
}

@Composable
private fun FloatingActionButtons(component: NetworkLogsComponent) {
    FloatingActionButton(onClick = component::clearLogs) {
        Icon(Icons.Default.Delete, null)
    }
}
