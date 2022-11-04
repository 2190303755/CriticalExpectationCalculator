package genshin.calculator.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import genshin.calculator.*

class MainViewModel : ViewModel() {
    val resultExpanded = MutableLiveData(true)
    var characterExpanded = MutableLiveData(false)
    var enhancementExpanded = MutableLiveData(false)
    var environmentExpanded = MutableLiveData(false)
    val characterRate = MutableLiveData(DOUBLE_5)
    val characterDamage = MutableLiveData(DOUBLE_50)
    val environmentRate = MutableLiveData(DOUBLE_0)
    val environmentDamage = MutableLiveData(DOUBLE_0)
    val inputCharacterRate = MutableLiveData(STRING_5)
    val inputCharacterDamage = MutableLiveData(STRING_50)
    val inputEnvironmentRate = MutableLiveData(STRING_0)
    val inputEnvironmentDamage = MutableLiveData(STRING_0)
    val expectation = MutableLiveData(DOUBLE_0)
    val artifactRate = MutableLiveData(DOUBLE_0)
    val artifactDamage = MutableLiveData(DOUBLE_0)
    val artifactType = MutableLiveData(INT_0)
    val flowerTimes = MutableLiveData(FLOAT_5)
    val plumeTimes = MutableLiveData(FLOAT_5)
    val sandsTimes = MutableLiveData(FLOAT_5)
    val gobletTimes = MutableLiveData(FLOAT_5)
    val circletTimes = MutableLiveData(FLOAT_5)
    fun update() {
        val result = ArtifactCriticalResult.create(
            characterRate.value!! + environmentRate.value!!,
            characterDamage.value!! + environmentDamage.value!!,
            (flowerTimes.value!! + plumeTimes.value!! + sandsTimes.value!! + gobletTimes.value!!).toInt(),
            circletTimes.value!!.toInt()
        )
        expectation.value = result.expectation
        artifactRate.value = result.rate
        artifactDamage.value = result.damage
        artifactType.value = result.type
    }
}