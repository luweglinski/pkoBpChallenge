package lw.pko.pkochallenge.core.theme

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AutoCompleteTextField(
    itemList: List<String>,
    onAutoCompleteSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean
) {
    LazyColumn(modifier = modifier.animateContentSize()) {
        if (itemList.isNotEmpty() && expanded) {
            items(itemList) { item ->
                Text(
                    item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .clickable(onClick = {
                            onAutoCompleteSelected(item)
                        })
                        .padding(4.dp)
                )
            }
        }
    }
}