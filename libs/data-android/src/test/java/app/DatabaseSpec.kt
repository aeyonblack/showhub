package app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Rule
import org.robolectric.annotation.Config

@Config(application = HiltTestApplication::class)
abstract class DatabaseSpec {

    @get:Rule(order = 0)
    val hiltRule: HiltAndroidRule by lazy { HiltAndroidRule(this) }

    @get:Rule(order = 1)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val testScope = TestCoroutineScope()

}