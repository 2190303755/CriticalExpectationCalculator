package genshin.calculator.core

import genshin.calculator.*
import kotlin.math.ceil
import kotlin.math.round

class ArtifactCriticalResult private constructor(
    type: Int,
    baseRate: Double,
    baseDamage: Double,
    circletRate: Double,
    circletDamage: Double,
    times: Double
) {
    val type: Int
    var expectation: Double = DOUBLE_0
    var rate: Double = DOUBLE_0
    var damage: Double = BASE_ARTIFACT_CRITICAL_DAMAGE

    init {
        var temp1: Double = ceil((DOUBLE_100 - baseRate - circletRate) / MAX_CRITICAL_RATE_PER_LORE)
        var temp2 = temp1.coerceIn(DOUBLE_0, DOUBLE_4)
        rate = temp2 * MAX_CRITICAL_RATE_PER_LORE + circletRate
        temp1 -= temp2
        damage += circletDamage
        if (temp1 > DOUBLE_0) {
            temp2 =
                round((times + (damage + baseDamage) / MAX_CRITICAL_DAMAGE_PER_LORE - (rate + baseDamage) / MAX_CRITICAL_RATE_PER_LORE) / DOUBLE_2)
                    .coerceIn(DOUBLE_0, times)
            if (temp2 > temp1--) {
                rate += temp1 * MAX_CRITICAL_RATE_PER_LORE
                damage += (times - temp1) * MAX_CRITICAL_DAMAGE_PER_LORE
                expectation = (rate + baseRate) * (damage + baseDamage) * DOUBLE_0_01
                temp1 = damage - MAX_CRITICAL_DAMAGE_PER_LORE
                temp2 = temp1 + baseDamage
                if (temp2 >= expectation) {
                    rate += MAX_CRITICAL_RATE_PER_LORE
                    damage = temp1
                    expectation = temp2
                }
                expectation *= DOUBLE_0_01
                this.type = type
            } else {
                rate += temp2 * MAX_CRITICAL_RATE_PER_LORE
                temp1 = rate + baseRate
                if (temp1 > DOUBLE_0) {
                    damage += (times - temp2) * MAX_CRITICAL_DAMAGE_PER_LORE
                    expectation = (damage + baseDamage) * temp1 * DOUBLE_0_0001
                    this.type = type
                } else {
                    rate = DOUBLE_0
                    damage = DOUBLE_0
                    expectation = DOUBLE_0
                    this.type = INT_0
                }
            }
        } else {
            damage = times * MAX_CRITICAL_DAMAGE_PER_LORE
            expectation = (damage + baseDamage) * DOUBLE_0_01
            this.type = type
        }
    }

    companion object {
        fun create(rate: Double, damage: Double, sum: Int, times: Int) =
            (DOUBLE_100 - BASE_ARTIFACT_CRITICAL_RATE - rate).let {
                if (it <= DOUBLE_0) {
                    ArtifactCriticalResult(
                        INT_1,
                        rate,
                        damage,
                        DOUBLE_0,
                        MAIN_LORE_CRITICAL_DAMAGE,
                        sum.toDouble()
                    )
                } else {
                    val temp1 = ArtifactCriticalResult(
                        INT_1,
                        rate,
                        damage,
                        ceil(it / MAX_CRITICAL_RATE_PER_LORE)
                            .coerceIn(
                                DOUBLE_0,
                                times.toDouble() + DOUBLE_1
                            ) * MAX_CRITICAL_RATE_PER_LORE,
                        MAIN_LORE_CRITICAL_DAMAGE,
                        sum.toDouble()
                    )
                    val temp2 = ArtifactCriticalResult(
                        INT_2,
                        rate,
                        damage,
                        MAIN_LORE_CRITICAL_RATE,
                        times * MAX_CRITICAL_DAMAGE_PER_LORE + MAX_CRITICAL_DAMAGE_PER_LORE,
                        sum.toDouble()
                    )
                    if (temp1.expectation > temp2.expectation) temp1 else temp2
                }
            }

    }
}