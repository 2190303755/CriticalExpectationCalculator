package genshin.calculator.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import genshin.calculator.INT_0
import genshin.calculator.ui.theme.Padding

private const val titleId = "title"
private const val summaryId = "summary"

@Composable
fun Description(
    title: String,
    summary: String?,
    modifier: Modifier = Modifier
) = Layout(
    content = {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.layoutId(titleId)
        )
        Box(modifier = Modifier.layoutId(summaryId)) {
            summary?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
        }
    },
    modifier = modifier
) { measurables, constraints ->
    val titlePlaceable = measurables.first { it.layoutId == titleId }
        .measure(constraints.copy(minWidth = INT_0))
    val summaryPlaceable = measurables.first { it.layoutId == summaryId }
        .measure(constraints.copy(minWidth = INT_0))
    val summaryStart = titlePlaceable.height + Padding.small.roundToPx()
    layout(width = constraints.maxWidth, height = summaryStart + summaryPlaceable.height) {
        titlePlaceable.placeRelative(
            x = INT_0,
            y = INT_0
        )
        summaryPlaceable.placeRelative(
            x = INT_0,
            y = summaryStart
        )
    }
}
