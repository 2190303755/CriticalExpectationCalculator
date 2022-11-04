package genshin.calculator.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

fun Modifier.listItemPadding(padding: Dp) = this.padding(
    start = padding,
    top = padding,
    end = padding
)