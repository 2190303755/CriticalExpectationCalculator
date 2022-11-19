package genshin.calculator.core

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import genshin.calculator.DOUBLE_0
import genshin.calculator.STRING_EMPTY

class DoubleTextFieldInput(initial: Double = DOUBLE_0) : LiveData<String>(initial.toString()) {
    var data: Double = initial
        private set
    var error: Boolean = false
        private set

    override fun getValue(): String = super.getValue() ?: STRING_EMPTY
    public override fun setValue(value: String?) {
        val result = value?.toDoubleOrNull()
        if (result == null) {
            error = true
        } else {
            error = false
            data = result
        }
        super.setValue(value)
    }

    @Composable
    fun observeInputAsState(): State<String> {
        val lifecycleOwner = LocalLifecycleOwner.current
        val state = remember { mutableStateOf(value) }
        DisposableEffect(this, lifecycleOwner) {
            val observer = Observer<String> { state.value = it }
            observe(lifecycleOwner, observer)
            onDispose { removeObserver(observer) }
        }
        return state
    }
}