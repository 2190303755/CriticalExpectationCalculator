package genshin.calculator.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import genshin.calculator.ui.theme.Padding

@Composable
fun Expander(
    title: String,
    summary: String?,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    icon: ImageVector? = null,
    indicator: ImageVector? = null,
    singleRow: Boolean = false,
    footer: @Composable RowScope.() -> Unit = {},
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    OutlinedCard(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.elevatedCardColors()
    ) {
        InfoRow(
            modifier = Modifier
                .padding(Padding.medium)
                .clip(MaterialTheme.shapes.medium)
                .clickable(onClick != null) {
                    onClick!!()
                }
                .padding(Padding.medium)
                .fillMaxWidth(),
            icon = icon,
            indicator = indicator,
            singleRow = singleRow,
            footer = footer
        ) {
            Description(title = title, summary = summary)
        }
        if (expanded) {
            content()
        }
    }
}