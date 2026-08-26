package com.idfinance.logkmpanion.presentation.ui.environment

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.start
import kotlinx.coroutines.Dispatchers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultEnvironmentComponentTest {

    private val lifecycle = LifecycleRegistry()

    private fun component(provider: FakeEnvironmentProvider): DefaultEnvironmentComponent {
        val component = DefaultEnvironmentComponent(
            context = DefaultComponentContext(lifecycle),
            provider = provider,
            coroutineContext = Dispatchers.Unconfined,
        )
        lifecycle.start()
        return component
    }

    @Test
    fun initialModel_takesEnvironmentsAndCustomHost_fromProvider() {
        val provider = FakeEnvironmentProvider()

        val model = component(provider).model.value

        assertEquals(TEST_ENVIRONMENTS, model.environments)
        assertEquals("pr-1.example.com", model.customHost)
        assertNull(model.error)
        assertFalse(model.isApplying)
    }

    @Test
    fun currentEmission_seedsSelection() {
        val provider = FakeEnvironmentProvider()

        val model = component(provider).model.value

        assertEquals("master", model.current?.id)
        assertEquals("master", model.selectedId)
    }

    @Test
    fun currentEmission_doesNotOverrideUserSelection() {
        val provider = FakeEnvironmentProvider()
        val component = component(provider)
        component.selectEnvironment("prod")

        provider.emitCurrent("master")

        assertEquals("prod", component.model.value.selectedId)
        assertEquals("master", component.model.value.current?.id)
    }

    @Test
    fun selectEnvironment_doesNotApply_untilApplyIsCalled() {
        val provider = FakeEnvironmentProvider()
        val component = component(provider)

        component.selectEnvironment("prod")

        assertEquals("prod", component.model.value.selectedId)
        assertTrue(provider.selectCalls.isEmpty())
    }

    @Test
    fun apply_passesNullHost_forNonEditableEnvironment() {
        val provider = FakeEnvironmentProvider()
        val component = component(provider)
        component.selectEnvironment("prod")

        component.apply()

        assertEquals(listOf(FakeEnvironmentProvider.SelectCall("prod", null)), provider.selectCalls)
    }

    @Test
    fun apply_passesTrimmedHost_forEditableEnvironment() {
        val provider = FakeEnvironmentProvider()
        val component = component(provider)
        component.selectEnvironment("custom")
        component.changeCustomHost("  pr-42.example.com  ")

        component.apply()

        assertEquals(
            listOf(FakeEnvironmentProvider.SelectCall("custom", "pr-42.example.com")),
            provider.selectCalls,
        )
    }

    @Test
    fun apply_isDisabledAndIgnored_whenSelectionDidNotChange() {
        val provider = FakeEnvironmentProvider()
        val component = component(provider)

        assertFalse(component.model.value.isApplyEnabled)
        component.apply()

        assertTrue(provider.selectCalls.isEmpty())
    }

    @Test
    fun apply_isDisabled_forEditableEnvironment_withBlankHost() {
        val provider = FakeEnvironmentProvider()
        val component = component(provider)
        component.selectEnvironment("custom")
        component.changeCustomHost("   ")

        assertFalse(component.model.value.isApplyEnabled)
        component.apply()

        assertTrue(provider.selectCalls.isEmpty())
    }

    @Test
    fun apply_surfacesProviderFailure_andStopsApplying() {
        val provider = FakeEnvironmentProvider(failure = IllegalStateException("bad host"))
        val component = component(provider)
        component.selectEnvironment("prod")

        component.apply()

        assertEquals("bad host", component.model.value.error)
        assertFalse(component.model.value.isApplying)
    }

    @Test
    fun changingSelection_clearsPreviousError() {
        val provider = FakeEnvironmentProvider(failure = IllegalStateException("bad host"))
        val component = component(provider)
        component.selectEnvironment("prod")
        component.apply()

        component.selectEnvironment("custom")

        assertNull(component.model.value.error)
    }
}
