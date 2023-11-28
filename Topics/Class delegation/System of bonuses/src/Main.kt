interface BonusCalculator {
    fun calculateBonus(spend: Double): Double
}

class FlatRateBonusCalculator(private val rate: Double) : BonusCalculator {
    override fun calculateBonus(spend: Double): Double {
        return (spend / 100.0).toInt() * rate
    }
}

class PercentageBonusCalculator(private val percentage: Double) : BonusCalculator {
    override fun calculateBonus(spend: Double): Double {
        return spend * percentage / 100.0
    }
}

class Customer(val name: String, bonusCalculator: BonusCalculator): BonusCalculator by bonusCalculator