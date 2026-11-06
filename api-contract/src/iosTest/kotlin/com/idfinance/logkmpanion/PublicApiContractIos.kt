package com.idfinance.logkmpanion

import com.idfinance.logkmpanion.presentation.LogKMPanionViewControllerProvider
import platform.UIKit.UIViewController

@Suppress("unused")
private fun callEveryIosApi(): UIViewController = LogKMPanionViewControllerProvider(onClose = {})
