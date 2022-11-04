package genshin.calculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import genshin.calculator.INT_0
import genshin.calculator.INT_2
import genshin.calculator.INT_24
import genshin.calculator.ui.theme.Padding
import kotlin.math.max

private const val IconId = "icon"
private const val HeaderId = "header"
private const val FooterId = "footer"
private const val IndicatorId = "indicator"
private val MaterialIconDimension = INT_24.dp

@Composable
fun InfoRow(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    indicator: ImageVector? = null,
    singleRow: Boolean = false,
    footer: @Composable RowScope.() -> Unit = {},
    header: @Composable BoxScope.() -> Unit
) {
    Layout(
        modifier = modifier,
        content = {
            Box(modifier = Modifier.layoutId(IconId)) {
                icon?.let { Icon(imageVector = it, contentDescription = null) }
            }
            Box(modifier = Modifier.layoutId(HeaderId), content = header)
            Row(modifier = Modifier.layoutId(FooterId), content = footer)
            Box(modifier = Modifier.layoutId(IndicatorId)) {
                indicator?.let { Icon(it, null) }
            }
        }
    ) { measurables, constraints ->
        val padding = Padding.medium.roundToPx()
        var contentMaxWidth = constraints.maxWidth
        val iconPlaceable = measurables.first { it.layoutId == IconId }
            .measure(constraints.copy(minWidth = INT_0))
        if (iconPlaceable.width != INT_0) {
            contentMaxWidth -= iconPlaceable.width + padding
        }
        val indicatorPlaceable = measurables.first { it.layoutId == IndicatorId }
            .measure(constraints.copy(minWidth = INT_0))
        if (indicatorPlaceable.width != INT_0) {
            contentMaxWidth -= indicatorPlaceable.width + padding
        }
        val footerPlaceable = measurables.first { it.layoutId == FooterId }
            .measure(
                constraints.copy(
                    minWidth = INT_0,
                    maxWidth = if (singleRow && constraints.maxWidth == Constraints.Infinity) contentMaxWidth / INT_2 else contentMaxWidth
                )
            )
        val singleRowHeaderMaxWidth =
            if (footerPlaceable.width != INT_0) contentMaxWidth - padding else contentMaxWidth
        if (singleRow || footerPlaceable.width * INT_2 <= singleRowHeaderMaxWidth) {
            val headerPlaceable = measurables.first { it.layoutId == HeaderId }
                .measure(constraints.copy(minWidth = INT_0, maxWidth = singleRowHeaderMaxWidth))
            val maxHeight =
                max(
                    max(headerPlaceable.height, footerPlaceable.height),
                    max(iconPlaceable.height, indicatorPlaceable.height)
                )
            val indicatorStart = constraints.maxWidth - indicatorPlaceable.width
            layout(constraints.maxWidth, maxHeight) {
                iconPlaceable.placeRelative(
                    x = INT_0,
                    y = (maxHeight - iconPlaceable.height) / INT_2
                )
                headerPlaceable.placeRelative(
                    x = iconPlaceable.width + padding,
                    y = (maxHeight - headerPlaceable.height) / INT_2
                )
                indicatorPlaceable.placeRelative(
                    x = indicatorStart,
                    y = (maxHeight - indicatorPlaceable.height) / INT_2
                )
                footerPlaceable.placeRelative(
                    x = (if (indicatorPlaceable.width != INT_0) indicatorStart - padding else constraints.maxWidth) - footerPlaceable.width,
                    y = (maxHeight - footerPlaceable.height) / INT_2
                )
            }
        } else {
            val indicatorStart = constraints.maxWidth - indicatorPlaceable.width
            val headerPlaceable = measurables.first { it.layoutId == HeaderId }
                .measure(constraints.copy(minWidth = INT_0, maxWidth = contentMaxWidth))
            val maxHeight = max(headerPlaceable.height, indicatorPlaceable.height)
            val footerStart = maxHeight + Padding.small.roundToPx()
            layout(constraints.maxWidth, footerStart + footerPlaceable.height) {
                iconPlaceable.placeRelative(
                    x = INT_0,
                    y = (maxHeight - iconPlaceable.height) / INT_2
                )
                headerPlaceable.placeRelative(
                    x = iconPlaceable.width + padding,
                    y = (maxHeight - headerPlaceable.height) / INT_2
                )
                indicatorPlaceable.placeRelative(
                    x = indicatorStart,
                    y = (maxHeight - indicatorPlaceable.height) / INT_2
                )
                footerPlaceable.placeRelative(
                    x = (if (indicatorPlaceable.width != INT_0) indicatorStart - padding else indicatorStart) - footerPlaceable.width,
                    y = footerStart
                )
            }
        }
    }
}

@Composable
fun SubInfoRow(
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    indicator: ImageVector? = null,
    footer: @Composable RowScope.() -> Unit = {}
) {
    Divider()
    Layout(
        modifier = modifier.padding(
            start = MaterialIconDimension,
            top = Padding.medium,
            bottom = Padding.medium
        ),
        content = {
            Description(
                title = title,
                summary = summary,
                modifier = Modifier.layoutId(HeaderId)
            )
            Row(modifier = Modifier.layoutId(FooterId), content = footer)
            Box(
                modifier = Modifier
                    .layoutId(IndicatorId)
                    .defaultMinSize(MaterialIconDimension, MaterialIconDimension)
            ) {
                indicator?.let { Icon(it, null) }
            }
        }
    ) { measurables, constraints ->
        val padding = Padding.medium.roundToPx()
        val indicatorPlaceable = measurables.first { it.layoutId == IndicatorId }
            .measure(constraints.copy(minWidth = INT_0))
        val contentMaxWidth = constraints.maxWidth - indicatorPlaceable.width - padding
        val footerPlaceable = measurables.first { it.layoutId == FooterId }
            .measure(constraints.copy(minWidth = INT_0, maxWidth = contentMaxWidth / INT_2))
        val headerPlaceable = measurables.first { it.layoutId == HeaderId }
            .measure(
                constraints.copy(
                    minWidth = INT_0,
                    maxWidth = if (footerPlaceable.width != INT_0) contentMaxWidth - padding else contentMaxWidth
                )
            )
        val maxHeight =
            max(max(headerPlaceable.height, footerPlaceable.height), indicatorPlaceable.height)
        layout(constraints.maxWidth, maxHeight) {
            headerPlaceable.placeRelative(
                x = INT_0,
                y = (maxHeight - headerPlaceable.height) / INT_2
            )
            indicatorPlaceable.placeRelative(
                x = contentMaxWidth + padding,
                y = (maxHeight - indicatorPlaceable.height) / INT_2
            )
            footerPlaceable.placeRelative(
                x = contentMaxWidth - footerPlaceable.width,
                y = (maxHeight - footerPlaceable.height) / INT_2
            )
        }
    }
}