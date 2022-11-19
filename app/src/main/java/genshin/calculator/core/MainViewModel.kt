package genshin.calculator.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import genshin.calculator.*

class MainViewModel : ViewModel() {
    val resultExpanded = MutableLiveData(true)
    var characterExpanded = MutableLiveData(false)
    var enhancementExpanded = MutableLiveData(false)
    var environmentExpanded = MutableLiveData(false)
    val characterRate = DoubleTextFieldInput(DOUBLE_5)
    val characterDamage = DoubleTextFieldInput(DOUBLE_50)
    val environmentRate = DoubleTextFieldInput(DOUBLE_0)
    val environmentDamage = DoubleTextFieldInput(DOUBLE_0)

    val flowerTimes = MutableLiveData(FLOAT_5)
    val plumeTimes = MutableLiveData(FLOAT_5)
    val sandsTimes = MutableLiveData(FLOAT_5)
    val gobletTimes = MutableLiveData(FLOAT_5)
    val circletTimes = MutableLiveData(FLOAT_5)

    fun extraRate() = characterRate.data + environmentRate.data
    fun extraDamage() = characterDamage.data + environmentDamage.data
    fun calculate() = ArtifactCriticalResult.create(
        extraRate(),
        extraDamage(),
        (flowerTimes.value!! + plumeTimes.value!! + sandsTimes.value!! + gobletTimes.value!!).toInt(),
        circletTimes.value!!.toInt()
    )

    val result = MutableLiveData<ArtifactCriticalResult>()
}