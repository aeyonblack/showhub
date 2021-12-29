package app.showhub.common.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T: Any> ItemGrid(
    columns: Int,
    rowWidth: Dp,
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T?) -> Unit
) {
    Surface(modifier) {
        LazyRow(
            modifier = Modifier.padding(16.dp)
        ) {
            items ((0 until columns).toList()) {
                Column {
                    (0..items.itemCount).forEach {
                        Surface(modifier = Modifier.width(rowWidth)) {
                            content(items[it])
                        }
                    }
                }
            }
        }
    }
}