# DebugView

This is the library that can be used to view logs in Android and iOS. Written in JetBrains Compose Multiplatform.

## How to integrate?

1) Add the dependency (`com.idfinance.kmm:debug-view`)
2) To save log call

```Kotlin
fun handleLog(type: LogType, tag: String, message: String)
```

3) To open log view:
- In Android:

  ```Kotlin
  fun openDebugView(context: Context)
  ```

- In iOS

  use

  ```Swift
  class DebugViewViewController(onClose: () -> Unit) : UIViewController(null, null)
  ```

## How to integrate the library only for debug builds?

The library also provides empty implementation (`debug-view-no-impl` module) with the same interface (package name and method class names) as `debug-view` module.
So, u can implement `debug-view` module in debug builds and use `debug-view-no-impl` in release mode

