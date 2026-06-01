package com.idfinance.logkmpanion.presentation.ui.allLogs

import androidx.core.content.FileProvider

/**
 * Dedicated [FileProvider] subclass with a unique class name.
 *
 * The Android manifest merger keys `<provider>` elements by `android:name`. Consumer apps
 * (especially Flutter ones) almost always bundle other libraries that declare their own
 * `androidx.core.content.FileProvider`, so a bare declaration would collide on the class name
 * and our authority + path meta-data would be dropped from the merged manifest — leading to
 * `IllegalArgumentException: Couldn't find meta-data for provider with authority ...` at runtime.
 *
 * Subclassing gives us a unique `android:name`, so our provider survives the merge intact.
 */
internal class LogKMPanionFileProvider : FileProvider()
