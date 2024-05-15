package com.storyteller_f.config_edit

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.storyteller_f.config_core.Config
import com.storyteller_f.config_core.Editor
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val file = File(appContext.filesDir, "test")
        file.delete()
        val configEditor = ConfigEditor<TestConfig>(appContext, null)
        val configAdapterFactory: RuntimeTypeAdapterFactory<Config> = RuntimeTypeAdapterFactory.of(
            Config::class.java, Config.DEFAULT_FACTORY_KEY
        ).registerSubtype(
            TestConfig::class.java, "filter-config"
        )
        val mock = mock(Editor.Listener::class.java)
        `when`(mock.onNew()).thenReturn(TestConfig())
        configEditor.init("test", mock as Editor.Listener<TestConfig>, configAdapterFactory)
        assert(configEditor.lastIndex == 0)
    }
}

class TestConfig : Config("test") {
    override fun dup(): Any {
        TODO("Not yet implemented")
    }

}