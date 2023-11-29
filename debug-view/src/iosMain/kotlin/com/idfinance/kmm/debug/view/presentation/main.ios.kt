package com.idfinance.kmm.debug.view.presentation // ktlint-disable filename

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.pause
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.idfinance.kmm.debug.view.ServiceLocator
import com.idfinance.kmm.debug.view.presentation.decompose.DebugComponent
import com.idfinance.kmm.debug.view.presentation.ui.LogView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationWillResignActiveNotification
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.willMoveToParentViewController
import platform.objc.sel_registerName

@Suppress("FunctionName")
private fun DebugViewViewController(component: DebugComponent) = ComposeUIViewController {
    LogView(component)
}

@Deprecated("Use DebugViewViewController() directly")
object DebugViewManager {
    fun getDebugViewViewController(): UIViewController {
        return DebugViewViewController()
    }
}

@Suppress("UNUSED")
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@ExportObjCClass
class DebugViewViewController : UIViewController(null, null) {
    private val lifecycle = LifecycleRegistry()
    private val component = ServiceLocator.getRootComponent(DefaultComponentContext(lifecycle))
    private val controller = DebugViewViewController(component)

    init {
        initObservers()
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        controller.willMoveToParentViewController(this)
        controller.view.setFrame(view.frame)
        view.addSubview(controller.view)
        addChildViewController(controller)
        controller.didMoveToParentViewController(this)
    }

    private fun initObservers() {
        NSNotificationCenter.defaultCenter.addObserver(
            this,
            sel_registerName("onResume"),
            UIApplicationDidBecomeActiveNotification,
            null,
        )
        NSNotificationCenter.defaultCenter.addObserver(
            this,
            sel_registerName("onPause"),
            UIApplicationWillResignActiveNotification,
            null,
        )
        NSNotificationCenter.defaultCenter.addObserver(
            this,
            sel_registerName("onStop"),
            UIApplicationDidEnterBackgroundNotification,
            null,
        )
    }

    @Suppress("UNUSED")
    @ObjCAction
    private fun onResume() {
        lifecycle.resume()
    }

    @Suppress("UNUSED")
    @ObjCAction
    private fun onPause() {
        lifecycle.pause()
    }

    @Suppress("UNUSED")
    @ObjCAction
    private fun onStop() {
        lifecycle.stop()
    }
}
