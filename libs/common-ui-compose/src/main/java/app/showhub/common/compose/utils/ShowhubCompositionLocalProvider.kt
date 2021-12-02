package app.showhub.common.compose.utils

import androidx.compose.runtime.staticCompositionLocalOf
import com.tanya.base.android.appinitializer.util.ShowhubDateFormatter

val LocalShowhubDateTimeFormatter = staticCompositionLocalOf<ShowhubDateFormatter> {
    error("ShowhubDateFormatter not provided")
}