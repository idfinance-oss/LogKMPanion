package com.idfinance.logkmpanion.presentation.ui

import platform.UIKit.UIViewController

@Suppress("FunctionName", "unused")
fun LogKMPanionViewControllerProvider(onClose: () -> Unit): UIViewController =
    LogKMPanionViewController(onClose)

@Suppress("UNUSED_PARAMETER")
private class LogKMPanionViewController(onClose: () -> Unit) : UIViewController(null, null)