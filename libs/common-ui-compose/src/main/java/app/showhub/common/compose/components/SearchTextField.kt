package app.showhub.common.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.showhub.common.compose.theme.pink500
import com.tanya.common.ui.resources.R.drawable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    hint: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                painter = painterResource(id = drawable.ic_search),
                contentDescription = null,
                tint = Color.Black.copy(0.7f),
                modifier = Modifier.size(16.dp)
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.text.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(onClick = { onValueChange(TextFieldValue()) }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = null,
                        tint = Color.Black.copy(0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.h5.copy(fontSize = 15.sp)
            ) },
        maxLines = 1,
        singleLine = true,
        shape = RoundedCornerShape(32.dp),
        textStyle = MaterialTheme.typography.h5.copy(fontSize = 15.sp),

        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Gray,
            placeholderColor = Color.Gray,
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledPlaceholderColor = Color.Gray,
            cursorColor = pink500.copy(alpha = 0.9f)
        ),
        modifier = modifier
    )
}