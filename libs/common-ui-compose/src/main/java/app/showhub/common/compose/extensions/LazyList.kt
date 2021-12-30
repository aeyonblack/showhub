package app.showhub.common.compose.extensions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import kotlin.math.ceil

fun LazyListScope.itemSpacer(height: Dp) {
    item {
        Spacer(
            Modifier
                .height(height)
                .fillParentMaxWidth()
        )
    }
}

fun LazyListScope.horizontalSpacer(width: Dp) {
    item {
        Spacer(modifier = Modifier.width(width))
    }
}


fun <T : Any> LazyListScope.horizontalGrid(
    lazyPagingItems: LazyPagingItems<T>,
    rows: Int,
    contentPadding: PaddingValues = PaddingValues(),
    horizontalItemPadding: Dp = 0.dp,
    verticalItemPadding: Dp = 0.dp,
    itemContent: @Composable LazyItemScope.(T?) -> Unit
) {
    val columns = ceil(lazyPagingItems.itemCount / rows.toDouble()).toInt()

    for (column in 0 until columns) {

        if (column == 0) horizontalSpacer(18.dp)

        item {
            Column(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                for (row in 0 until rows) {
                    val index = (column * rows) + row
                    if (index < lazyPagingItems.itemCount) {
                        itemContent(lazyPagingItems[index])
                    }
                    if (row < rows - 1) Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        if (column >= columns - 1) horizontalSpacer(21.dp)
    }
}

fun <T : Any> LazyListScope.itemsInGrid(
    lazyPagingItems: LazyPagingItems<T>,
    columns: Int,
    contentPadding: PaddingValues = PaddingValues(),
    horizontalItemPadding: Dp = 0.dp,
    verticalItemPadding: Dp = 0.dp,
    itemContent: @Composable LazyItemScope.(T?) -> Unit
) {
    val rows = when {
        lazyPagingItems.itemCount % columns == 0 -> lazyPagingItems.itemCount / columns
        else -> (lazyPagingItems.itemCount / columns) + 1
    }

    for (row in 0 until rows) {
        if (row == 0) itemSpacer(contentPadding.calculateTopPadding())

        item {
            val layoutDirection = LocalLayoutDirection.current
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = contentPadding.calculateStartPadding(layoutDirection),
                        end = contentPadding.calculateEndPadding(layoutDirection)
                    )
            ) {
                for (column in 0 until columns) {
                    Box(modifier = Modifier.weight(1f)) {
                        val index = (row * columns) + column
                        if (index < lazyPagingItems.itemCount) {
                            itemContent(lazyPagingItems[index])
                        }
                    }
                    if (column < columns - 1) {
                        Spacer(modifier = Modifier.width(horizontalItemPadding))
                    }
                }
            }
        }

        if (row < rows - 1) {
            itemSpacer(verticalItemPadding)
        } else {
            itemSpacer(contentPadding.calculateBottomPadding())
        }
    }
}

fun <T : Any> LazyListScope.itemsInGridIndexed(
    lazyPagingItems: LazyPagingItems<T>,
    columns: Int,
    contentPadding: PaddingValues = PaddingValues(),
    horizontalItemPadding: Dp = 0.dp,
    verticalItemPadding: Dp = 0.dp,
    itemContent: @Composable LazyItemScope.(T?, index: Int) -> Unit
) {
    val rows = when {
        lazyPagingItems.itemCount % columns == 0 -> lazyPagingItems.itemCount / columns
        else -> (lazyPagingItems.itemCount / columns) + 1
    }

    for (row in 0 until rows) {
        if (row == 0) itemSpacer(contentPadding.calculateTopPadding())

        item {
            val layoutDirection = LocalLayoutDirection.current
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = contentPadding.calculateStartPadding(layoutDirection),
                        end = contentPadding.calculateEndPadding(layoutDirection)
                    )
            ) {
                for (column in 0 until columns) {
                    Box(modifier = Modifier.weight(1f)) {
                        val index = (row * columns) + column
                        if (index < lazyPagingItems.itemCount) {
                            itemContent(lazyPagingItems[index], index)
                        }
                    }
                    if (column < columns - 1) {
                        Spacer(modifier = Modifier.width(horizontalItemPadding))
                    }
                }
            }
        }

        if (row < rows - 1) {
            itemSpacer(verticalItemPadding)
        }
    }
}

fun <T> LazyListScope.itemsInGrid(
    items: List<T>,
    columns: Int,
    contentPadding: PaddingValues = PaddingValues(),
    horizontalItemPadding: Dp = 0.dp,
    verticalItemPadding: Dp = 0.dp,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    val rows = when {
        items.size % columns == 0 -> items.size / columns
        else -> (items.size / columns) + 1
    }

    for (row in 0 until rows) {
        if (row == 0) itemSpacer(contentPadding.calculateTopPadding())

        item {
            val layoutDirection = LocalLayoutDirection.current

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = contentPadding.calculateStartPadding(layoutDirection),
                        end = contentPadding.calculateEndPadding(layoutDirection)
                    )
            ) {
                for (column in 0 until columns) {
                    Box(modifier = Modifier.weight(1f)) {
                        val index = (row * columns) + column
                        if (index < items.size) {
                            itemContent(items[index])
                        }
                    }
                    if (column < columns - 1) {
                        Spacer(modifier = Modifier.width(horizontalItemPadding))
                    }
                }
            }
        }

        if (row < rows - 1) {
            itemSpacer(verticalItemPadding)
        } else {
            itemSpacer(contentPadding.calculateBottomPadding())
        }
    }
}
