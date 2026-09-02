package com.idfinance.logkmpanion

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.idfinance.logkmpanion.presentation.ui.environment.FakeEnvironmentProvider
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LogKMPanionTest {

    @AfterTest
    fun tearDown() {
        LogKMPanion.setEnvironmentProvider(null)
    }

    @Test
    fun environmentTab_isUnavailable_whenNoProviderIsRegistered() {
        LogKMPanion.setEnvironmentProvider(null)

        assertFalse(ServiceLocator.isEnvironmentAvailable)
        assertNull(ServiceLocator.getEnvironmentComponent(componentContext()))
    }

    @Test
    fun environmentTab_isAvailable_whenProviderIsRegistered() {
        LogKMPanion.setEnvironmentProvider(FakeEnvironmentProvider())

        assertTrue(ServiceLocator.isEnvironmentAvailable)
        assertNotNull(ServiceLocator.getEnvironmentComponent(componentContext()))
    }

    private fun componentContext() = DefaultComponentContext(LifecycleRegistry())
}
