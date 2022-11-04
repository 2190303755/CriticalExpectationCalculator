package genshin.calculator.core

import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import genshin.calculator.*
import genshin.calculator.R
import genshin.calculator.ui.Expander
import genshin.calculator.ui.SubInfoRow
import genshin.calculator.ui.listItemPadding
import genshin.calculator.ui.theme.DynamicColorTheme
import genshin.calculator.ui.theme.Padding

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
class MainActivity : ComponentActivity() {
    private val systemBarsInsets: MutableLiveData<Insets> =
        MutableLiveData(Insets.of(INT_0, INT_0, INT_0, INT_0))
    private var isIndicatorUsed = false
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.update()
        val percentageKeyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done
        )
        systemBarsInsets.observe(this) {
            window.decorView.updatePadding(
                top = INT_0,
                bottom = if (isIndicatorUsed) INT_0 else it.bottom,
                left = it.left,
                right = it.right
            )
            isIndicatorUsed =
                when (Settings.Secure.getInt(contentResolver, STRING_NAV_MODE, INT_0)) {
                    INDICATOR_NAV_MODE_ANDROID, INDICATOR_NAV_MODE_HARMONY -> true
                    else -> false
                }
        }
        window.decorView.setOnApplyWindowInsetsListener { _, windowInsets ->
            systemBarsInsets.value = WindowInsetsCompat.toWindowInsetsCompat(windowInsets)
                .getInsets(WindowInsetsCompat.Type.systemBars())
            windowInsets
        }
        setContent {
            DynamicColorTheme {
                val scrollBehavior =
                    TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                val sliderRange = FLOAT_0..FLOAT_5
                val keyboardController = LocalSoftwareKeyboardController.current
                systemBarsInsets.observeAsState().value!!.let { insets ->
                    Scaffold(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = stringResource(R.string.app_name),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                },
                                scrollBehavior = scrollBehavior,
                                windowInsets = WindowInsets(top = insets.top)
                            )
                        },
                        contentWindowInsets = WindowInsets(
                            left = insets.left,
                            top = insets.top,
                            right = insets.right,
                            bottom = if (isIndicatorUsed) INT_0 else insets.bottom
                        )
                    ) { rootPadding ->
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(rootPadding)
                                .verticalScroll(rememberScrollState())
                        ) {
                            val expectation = viewModel.expectation.observeAsState()
                            val artifactRate = viewModel.artifactRate.observeAsState()
                            val artifactDamage = viewModel.artifactDamage.observeAsState()
                            val artifactType = viewModel.artifactType.observeAsState()
                            val characterRate = viewModel.characterRate.observeAsState()
                            val characterDamage = viewModel.characterDamage.observeAsState()
                            val environmentRate = viewModel.environmentRate.observeAsState()
                            val environmentDamage = viewModel.environmentDamage.observeAsState()
                            val inputCharacterRate = viewModel.inputCharacterRate.observeAsState()
                            val inputCharacterDamage =
                                viewModel.inputCharacterDamage.observeAsState()
                            val inputEnvironmentRate =
                                viewModel.inputEnvironmentRate.observeAsState()
                            val inputEnvironmentDamage =
                                viewModel.inputEnvironmentDamage.observeAsState()
                            var characterRateError by remember { mutableStateOf(false) }
                            var characterDamageError by remember { mutableStateOf(false) }
                            var environmentRateError by remember { mutableStateOf(false) }
                            var environmentDamageError by remember { mutableStateOf(false) }
                            val flowerTimes = viewModel.flowerTimes.observeAsState()
                            val plumeTimes = viewModel.plumeTimes.observeAsState()
                            val sandsTimes = viewModel.sandsTimes.observeAsState()
                            val gobletTimes = viewModel.gobletTimes.observeAsState()
                            val circletTimes = viewModel.circletTimes.observeAsState()
                            val resultExpanded = viewModel.resultExpanded.observeAsState()
                            val characterExpanded = viewModel.characterExpanded.observeAsState()
                            val enhancementExpanded = viewModel.enhancementExpanded.observeAsState()
                            val environmentExpanded = viewModel.environmentExpanded.observeAsState()
                            Expander(title = stringResource(R.string.result),
                                summary = null,
                                expanded = resultExpanded.value!!,
                                modifier = Modifier.listItemPadding(Padding.large),
                                onClick = {
                                    viewModel.resultExpanded.value =
                                        !viewModel.resultExpanded.value!!
                                },
                                indicator = if (resultExpanded.value!!) {
                                    Icons.Outlined.KeyboardArrowUp
                                } else {
                                    Icons.Outlined.KeyboardArrowDown
                                },
                                footer = {
                                    IconButton(onClick = {
                                        viewModel.inputCharacterRate.value?.toDoubleOrNull()?.let {
                                            viewModel.characterRate.value = it
                                        }
                                        viewModel.inputCharacterDamage.value?.toDoubleOrNull()
                                            ?.let {
                                                viewModel.characterDamage.value = it
                                            }
                                        viewModel.inputEnvironmentRate.value?.toDoubleOrNull()
                                            ?.let {
                                                viewModel.environmentRate.value = it
                                            }
                                        viewModel.inputEnvironmentDamage.value?.toDoubleOrNull()
                                            ?.let {
                                                viewModel.environmentDamage.value = it
                                            }
                                        viewModel.update()
                                        viewModel.resultExpanded.value = true
                                    }) {
                                        Icon(Icons.Outlined.Refresh, null)
                                    }
                                }) {
                                SubInfoRow(stringResource(R.string.expectation)) {
                                    Text(
                                        text = stringResource(
                                            R.string.percentage_5, expectation.value!!
                                        )
                                    )
                                }
                                SubInfoRow(
                                    stringResource(
                                        R.string.percentage_description,
                                        stringResource(R.string.critical_rate)
                                    )
                                ) {
                                    Text(
                                        text = stringResource(
                                            R.string.percentage_1,
                                            characterRate.value!! + environmentRate.value!! + artifactRate.value!!
                                        )
                                    )
                                }
                                SubInfoRow(
                                    stringResource(
                                        R.string.percentage_description,
                                        stringResource(R.string.critical_damage)
                                    )
                                ) {
                                    Text(
                                        text = stringResource(
                                            R.string.percentage_1,
                                            characterDamage.value!! + environmentDamage.value!! + artifactDamage.value!!
                                        )
                                    )
                                }
                                SubInfoRow(stringResource(R.string.main_lore)) {
                                    Text(
                                        text = when (artifactType.value!!) {
                                            INT_1 -> stringResource(R.string.critical_damage)
                                            INT_2 -> stringResource(R.string.critical_rate)
                                            else -> stringResource(R.string.unknown)
                                        }
                                    )
                                }
                                SubInfoRow(
                                    stringResource(
                                        R.string.percentage_description,
                                        stringResource(R.string.artifact_critical_rate)
                                    )
                                ) {
                                    Text(
                                        text = stringResource(
                                            R.string.percentage_1, artifactRate.value!!
                                        )
                                    )
                                }
                                SubInfoRow(
                                    stringResource(
                                        R.string.percentage_description,
                                        stringResource(R.string.artifact_critical_damage)
                                    )
                                ) {
                                    Text(
                                        text = stringResource(
                                            R.string.percentage_1, artifactDamage.value!!
                                        )
                                    )
                                }
                            }
                            Expander(
                                title = stringResource(R.string.character),
                                summary = null,
                                expanded = characterExpanded.value!!,
                                modifier = Modifier.listItemPadding(Padding.large),
                                onClick = {
                                    viewModel.characterExpanded.value =
                                        !viewModel.characterExpanded.value!!
                                },
                                indicator = if (characterExpanded.value!!) {
                                    Icons.Outlined.KeyboardArrowUp
                                } else {
                                    Icons.Outlined.KeyboardArrowDown
                                }
                            ) {
                                SubInfoRow(
                                    stringResource(
                                        R.string.percentage_description,
                                        stringResource(R.string.critical_rate)
                                    )
                                ) {
                                    OutlinedTextField(value = inputCharacterRate.value!!,
                                        onValueChange = {
                                            viewModel.inputCharacterRate.value = it
                                            val value = it.toDoubleOrNull()
                                            if (value == null) {
                                                characterRateError = true
                                            } else {
                                                characterRateError = false
                                                viewModel.characterRate.value = value
                                                viewModel.update()
                                            }
                                        },
                                        isError = characterRateError,
                                        keyboardOptions = percentageKeyboardOptions,
                                        keyboardActions = KeyboardActions { keyboardController?.hide() })
                                }
                                SubInfoRow(
                                    stringResource(
                                        R.string.percentage_description,
                                        stringResource(R.string.critical_damage)
                                    )
                                ) {
                                    OutlinedTextField(value = inputCharacterDamage.value!!,
                                        onValueChange = {
                                            viewModel.inputCharacterDamage.value = it
                                            val value = it.toDoubleOrNull()
                                            if (value == null) {
                                                characterDamageError = true
                                            } else {
                                                characterDamageError = false
                                                viewModel.characterDamage.value = value
                                                viewModel.update()
                                            }
                                        },
                                        isError = characterDamageError,
                                        keyboardOptions = percentageKeyboardOptions,
                                        keyboardActions = KeyboardActions { keyboardController?.hide() })
                                }
                            }
                            Expander(
                                title = stringResource(R.string.enhancement),
                                summary = null,
                                expanded = enhancementExpanded.value!!,
                                modifier = Modifier.listItemPadding(Padding.large),
                                onClick = {
                                    viewModel.enhancementExpanded.value =
                                        !viewModel.enhancementExpanded.value!!
                                },
                                indicator = if (enhancementExpanded.value!!) {
                                    Icons.Outlined.KeyboardArrowUp
                                } else {
                                    Icons.Outlined.KeyboardArrowDown
                                }
                            ) {
                                SubInfoRow(stringResource(R.string.flower)) {
                                    Slider(
                                        value = flowerTimes.value!!, onValueChange = {
                                            viewModel.flowerTimes.value = it
                                            viewModel.update()
                                        }, valueRange = sliderRange, steps = INT_4
                                    )
                                }
                                SubInfoRow(stringResource(R.string.plume)) {
                                    Slider(
                                        value = plumeTimes.value!!.toFloat(), onValueChange = {
                                            viewModel.plumeTimes.value = it
                                            viewModel.update()
                                        }, valueRange = sliderRange, steps = INT_4
                                    )
                                }
                                SubInfoRow(stringResource(R.string.sands)) {
                                    Slider(
                                        value = sandsTimes.value!!, onValueChange = {
                                            viewModel.sandsTimes.value = it
                                            viewModel.update()
                                        }, valueRange = sliderRange, steps = INT_4
                                    )
                                }
                                SubInfoRow(stringResource(R.string.goblet)) {
                                    Slider(
                                        value = gobletTimes.value!!, onValueChange = {
                                            viewModel.gobletTimes.value = it
                                            viewModel.update()
                                        }, valueRange = sliderRange, steps = INT_4
                                    )
                                }
                                SubInfoRow(stringResource(R.string.circlet)) {
                                    Slider(
                                        value = circletTimes.value!!, onValueChange = {
                                            viewModel.circletTimes.value = it
                                            viewModel.update()
                                        }, valueRange = sliderRange, steps = INT_4
                                    )
                                }
                            }
                            Expander(
                                title = stringResource(R.string.environment),
                                summary = null,
                                expanded = environmentExpanded.value!!,
                                modifier = Modifier.padding(Padding.large),
                                onClick = {
                                    viewModel.environmentExpanded.value =
                                        !viewModel.environmentExpanded.value!!
                                },
                                indicator = if (environmentExpanded.value!!) {
                                    Icons.Outlined.KeyboardArrowUp
                                } else {
                                    Icons.Outlined.KeyboardArrowDown
                                }
                            ) {
                                SubInfoRow(
                                    stringResource(
                                        R.string.percentage_description,
                                        stringResource(R.string.critical_rate)
                                    )
                                ) {
                                    OutlinedTextField(value = inputEnvironmentRate.value!!,
                                        onValueChange = {
                                            viewModel.inputEnvironmentRate.value = it
                                            val value = it.toDoubleOrNull()
                                            if (value == null) {
                                                environmentRateError = true
                                            } else {
                                                environmentRateError = false
                                                viewModel.environmentRate.value = value
                                                viewModel.update()
                                            }
                                        },
                                        isError = environmentRateError,
                                        keyboardOptions = percentageKeyboardOptions,
                                        keyboardActions = KeyboardActions { keyboardController?.hide() })
                                }
                                SubInfoRow(
                                    stringResource(
                                        R.string.percentage_description,
                                        stringResource(R.string.critical_damage)
                                    )
                                ) {
                                    OutlinedTextField(value = inputEnvironmentDamage.value!!,
                                        onValueChange = {
                                            viewModel.inputEnvironmentDamage.value = it
                                            val value = it.toDoubleOrNull()
                                            if (value == null) {
                                                environmentDamageError = true
                                            } else {
                                                environmentDamageError = false
                                                viewModel.environmentDamage.value = value
                                                viewModel.update()
                                            }
                                        },
                                        isError = environmentDamageError,
                                        keyboardOptions = percentageKeyboardOptions,
                                        keyboardActions = KeyboardActions { keyboardController?.hide() })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}