package com.idfinance.logkmpanion.presentation.ui.environment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.idfinance.logkmpanion.domain.environment.DebugEnvironment

@Composable
internal fun EnvironmentView(component: EnvironmentComponent) {
    val model by component.model.subscribeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Current: ${model.current?.title ?: "unknown"} (${model.current?.host.orEmpty()})",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        model.environments.forEach { environment ->
            EnvironmentItem(
                environment = environment,
                isSelected = environment.id == model.selectedId,
                onClick = { component.selectEnvironment(environment.id) },
            )
        }

        if (model.isCustomHostVisible) {
            OutlinedTextField(
                value = model.customHost,
                onValueChange = component::changeCustomHost,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Host") },
                placeholder = { Text("my-app-pr-123.example.com") },
                singleLine = true,
                isError = model.error != null,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done,
                ),
            )
        }

        model.error?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Button(
            onClick = component::apply,
            modifier = Modifier.fillMaxWidth(),
            enabled = model.isApplyEnabled,
        ) {
            if (model.isApplying) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
            } else {
                Text("Apply")
            }
        }
    }
}

@Composable
private fun EnvironmentItem(
    environment: DebugEnvironment,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = environment.title,
                style = MaterialTheme.typography.bodyMedium,
            )
            if (!environment.isHostEditable) {
                Text(
                    text = environment.host,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
