package app.showhub.common.compose.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import app.showhub.common.compose.extensions.bodyWidth
import app.showhub.common.compose.extensions.itemsInGrid
import app.showhub.common.compose.utils.Layout
import com.tanya.data.Entry
import com.tanya.data.results.EntryWithShow

@Composable
fun <E: Entry> LazyGrid(
    list: LazyPagingItems<out EntryWithShow<E>>,
    contentPadding: PaddingValues,
    openShowDetails: (showId: Long) -> Unit,
    optionsRow: (@Composable () -> Unit)? = null
) {
    val columns = Layout.columns
    val bodyMargin = Layout.bodyMargin
    val gutter = Layout.gutter

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.bodyWidth().fillMaxHeight(),
    ) {

        if (optionsRow != null) {
            item { optionsRow() }
        }

        itemsInGrid(
            lazyPagingItems = list,
            columns = columns / 2,
            contentPadding = PaddingValues(horizontal = bodyMargin, vertical = gutter),
            verticalItemPadding = gutter,
            horizontalItemPadding = gutter,
        ) { entry ->
            val mod = Modifier
                .aspectRatio(2 / 3f)
                .fillMaxWidth()
            if (entry != null) {
                PosterCard(
                    showTitle = entry.show.title,
                    posterPath = entry.poster?.path,
                    onClick = { openShowDetails(entry.show.id) },
                    modifier = mod
                )
            } else {
                // TODO: Add shimmer animation
            }
        }

        if (list.loadState.append == LoadState.Loading) {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        }
    }

}