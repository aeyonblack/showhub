package app.showhub.common.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.tanya.common.ui.resources.R.*

@Composable
fun AppBar() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Text(
            text = "Hey there!",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = drawable.ic_refresh),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 16.dp
                    )
            )
            Image(
                painter = painterResource(id = drawable.ic_settings),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 16.dp
                    )
            )
        }
    }
}