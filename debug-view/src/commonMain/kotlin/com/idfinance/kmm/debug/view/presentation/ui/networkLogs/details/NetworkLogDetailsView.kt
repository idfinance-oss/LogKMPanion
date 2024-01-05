package com.idfinance.kmm.debug.view.presentation.ui.networkLogs.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
internal fun NetworkLogDetailsView(component: NetworkLogDetailsComponent) {
    val model by component.model.subscribeAsState()

    Column(
        modifier = Modifier.padding(bottom = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row {
            Text(
                "Method: ",
                color = MaterialTheme.colorScheme.primary
            )
            Text(model.method)
        }
        Row(modifier = Modifier.padding(top = 20.dp)) {
            Text(
                "URL: ",
                color = MaterialTheme.colorScheme.primary
            )
            Text(model.url)
        }

        model.statusCode?.let {
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Text(
                    "Status Code: ",
                    color = MaterialTheme.colorScheme.primary
                )
                Text(it.toString())
            }
        }

        model.duration?.let {
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Text(
                    "Duration: ",
                    color = MaterialTheme.colorScheme.primary
                )
                Text("$it ms")
            }
        }

        model.request?.let {
            Text(
                "Request",
                modifier = Modifier.padding(top = 20.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Row {
                Text(
                    "Body: ",
                    color = MaterialTheme.colorScheme.primary
                )
                Text(it.body)
            }
            Text(
                "Cookies:",
                modifier = Modifier.padding(top = 20.dp),
                color = MaterialTheme.colorScheme.primary
            )
            it.cookies.forEach { Text(it) }
            Text(
                "Headers:",
                modifier = Modifier.padding(top = 20.dp),
                color = MaterialTheme.colorScheme.primary
            )
            it.headers.forEach { Text(it) }
        }

        model.response?.let {
            Text(
                "Response",
                modifier = Modifier.padding(top = 20.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Row {
                Text(
                    "Time: ",
                    color = MaterialTheme.colorScheme.primary
                )
                Text(it.time)
            }
            Row(
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(
                    "Body: ",
                    color = MaterialTheme.colorScheme.primary
                )
                Text(it.body)
            }
            Text(
                "Headers:",
                modifier = Modifier.padding(top = 20.dp),
                color = MaterialTheme.colorScheme.primary
            )
            it.headers.forEach { Text(it) }
        }

        model.error?.let {
            Text(
                "Error",
                modifier = Modifier.padding(top = 20.dp),
                color = MaterialTheme.colorScheme.error
            )
            Text(it)
        }
    }
}