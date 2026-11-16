package com.Zihmaho6.Tekloc.domain.model

enum class Station { BURNER, COOLING_RACK, PREP_TABLE }

data class StationTiers(
    val burner: Int = 0,
    val cooling: Int = 0,
    val prep: Int = 0
) {
    fun of(station: Station): Int = when (station) {
        Station.BURNER -> burner
        Station.COOLING_RACK -> cooling
        Station.PREP_TABLE -> prep
    }

    fun with(station: Station, tier: Int): StationTiers = when (station) {
        Station.BURNER -> copy(burner = tier)
        Station.COOLING_RACK -> copy(cooling = tier)
        Station.PREP_TABLE -> copy(prep = tier)
    }
}

data class KitchenProgress(
    val coins: Int = 0,
    val shifts: Int = 0,
    val plates: Int = 0,
    val perfects: Int = 0,
    val unlockedLevels: Int = 1,
    val lastLevel: Int = 1,
    val tiers: StationTiers = StationTiers(),
    val stars: Map<Int, Int> = emptyMap(),
    val bestScores: Map<Int, Int> = emptyMap(),
    val mastery: Map<Int, Int> = emptyMap()
) {
    val totalStars: Int get() = stars.values.sum()
    val unlockedRecipes: Int get() = mastery.size
    val perfectRate: Int get() = if (plates <= 0) 0 else (perfects * 100) / plates
    val topTier: Int get() = maxOf(tiers.burner, tiers.cooling, tiers.prep)
}
