# Shun

[![](https://jitpack.io/v/storytellerF/Shun.svg)](https://jitpack.io/#storytellerF/Shun)

Core 封装操作逻辑。
提供两种类型Core，分别是Filter和SortChain。

预先提供的Filter 有DateRangeFilter，ValueRangeFilter，RegexpFilter。

持久化的数据存储到**ConfigItem** 中，一般来说ConfigItem 是Core 的成员。

## 使用方式

需要提供ConfigItem 和Core 的转换

```kotlin
interface Listener<O, CItem : ConfigItem> {
   fun onSaveState(oList: List<O>): List<CItem>

   fun onRestoreState(configItems: List<CItem>): List<O>

   fun onActiveChanged(activeList: List<O>)

   fun onEditingChanged(editing: List<O>)
}
```

Active 和Editing的区别在于前者已经持久化的磁盘中

如果需要Observe 上述两者，可以使用**listenerWrapper** 包裹Listener，此方法会返回两个StateFlow，分别对应Active 和Editing。

如果不需要Observe，也可以通过**EditorKey.editor** 手动创建Editor 一次性获取配置。

1. Android View

   继承SortDialogFragment 和FilterDialogFragment

2. Jetpack Compose
   
   使用FilterDialog 和SortDialog

