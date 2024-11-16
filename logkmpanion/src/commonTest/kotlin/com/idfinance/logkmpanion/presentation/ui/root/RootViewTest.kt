package com.idfinance.logkmpanion.presentation.ui.root

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.idfinance.logkmpanion.ServiceLocator
import com.idfinance.logkmpanion.presentation.ui.allLogs.AllLogsTestTags
import com.idfinance.logkmpanion.presentation.ui.networkLogs.NetworkLogsTestTags
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class RootViewTest {

    @Test
    fun rootViewPresents() = runComposeUiTest {
        setContent {
            RootView(
                ServiceLocator.getRootComponent(
                    context = DefaultComponentContext(LifecycleRegistry()),
                    onClose = {}
                )
            )
        }

        onNodeWithTag(RootViewTestTags.ROOT_VIEW).isDisplayed()
    }

    @Test
    fun tabsSwitched() = runComposeUiTest {
        setContent {
            RootView(
                ServiceLocator.getRootComponent(
                    context = DefaultComponentContext(LifecycleRegistry()),
                    onClose = {}
                )
            )
        }

        onNodeWithText("Network logs").performClick()
        onNodeWithTag(NetworkLogsTestTags.ROOT_VIEW).assertExists()
        onNodeWithTag(AllLogsTestTags.ROOT_VIEW).assertDoesNotExist()

        onNodeWithText("All logs").performClick()
        onNodeWithTag(AllLogsTestTags.ROOT_VIEW).assertExists()
        onNodeWithTag(NetworkLogsTestTags.ROOT_VIEW).assertDoesNotExist()
    }
}