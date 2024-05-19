package com.storyteller_f.config_edit

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.storyteller_f.config_core.Config
import com.storyteller_f.config_core.ConfigItem
import com.storyteller_f.config_core.Editor
import com.storyteller_f.config_core.EditorKey
import com.storyteller_f.config_core.editor
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun testJsonParse() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val file = File(appContext.filesDir, "test")
        file.delete()
        val configEditor = createEditorConfig(appContext)
        assert(configEditor.lastIndex == 0)
        configEditor.save()
        val name = configEditor.lastConfig?.name
        val config2 = createEditorConfig(appContext)
        assertEquals(name, config2.lastConfig?.name)
    }

    @Test
    fun testCommand() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val file = File(appContext.filesDir, "test")
        file.delete()
        val factories = factoryPair()
        val editor = EditorKey.createEditorKey(appContext.filesDir.absolutePath, "test")
            .editor(object : Editor.Listener<TestConfig> {
                override fun onConfigSelectedChanged(configIndex: Int, config: TestConfig?) {

                }

                override fun onConfigChanged(list: List<Config>) {
                }

                override fun onNew(): TestConfig {
                    return TestConfig()
                }
            }, *factories)
        editor.sendCommand(Editor.COMMAND_CLONE, 1)
        assertEquals(1, editor.lastIndex)
        editor.sendCommand(Editor.COMMAND_DELETE, 2)
        assertEquals(0, editor.lastIndex)
        editor.sendCommand(Editor.COMMAND_NEW, 1)
        assertEquals(1, editor.lastIndex)
    }

    private fun createEditorConfig(appContext: Context): ConfigEditor<TestConfig> {
        val configEditor = ConfigEditor<TestConfig>(appContext, null)
        val factories = factoryPair()
        val mockk = mockk<Editor.Listener<TestConfig>> {
            every {
                onNew()
            } returns TestConfig()
            every {
                onConfigChanged(any())
            } just runs
            every {
                onConfigSelectedChanged(any(), any())
            } just runs
        }
        configEditor.init("test", mockk, *factories)
        return configEditor
    }

    private fun factoryPair(): Array<RuntimeTypeAdapterFactory<out Any>> {
        val configFactory: RuntimeTypeAdapterFactory<Config> = RuntimeTypeAdapterFactory.of(
            Config::class.java, Config.DEFAULT_FILTER_FACTORY_KEY
        ).registerSubtype(
            TestConfig::class.java, "test-config"
        )
        val itemFactory: RuntimeTypeAdapterFactory<ConfigItem> = RuntimeTypeAdapterFactory.of(
            ConfigItem::class.java, ConfigItem.DEFAULT_SORT_FACTORY_KEY
        ).registerSubtype(
            TestConfigItem::class.java, "test-item"
        )
        return arrayOf(configFactory, itemFactory)
    }
}

class TestConfig : Config("test") {
    override fun dup(): Any {
        return TestConfig()
    }

}

class TestConfigItem : ConfigItem(1, "") {
    override fun dup(): Any {
        return TestConfigItem()
    }

}