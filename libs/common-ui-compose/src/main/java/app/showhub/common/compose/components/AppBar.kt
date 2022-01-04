package app.showhub.common.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.tanya.common.ui.resources.R.drawable

@Composable
fun AppBar(
    title: String,
    onRefresh: (() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = { onRefresh?.invoke() }
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_refresh),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }

            /*IconButton(
                onClick = { *//*TODO*//* }
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_user),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }

            IconButton(
                modifier = Modifier.padding(
                    end = 8.dp
                ),
                onClick = { *//*TODO*//* }
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_settings_alt),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }*/
        }
    }
}