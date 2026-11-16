package com.Zihmaho6.Tekloc.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Zihmaho6.Tekloc.domain.model.GameSettings
import com.Zihmaho6.Tekloc.domain.model.KitchenProgress
import com.Zihmaho6.Tekloc.domain.model.StationTiers

class KitchenPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun loadProgress(): KitchenProgress = KitchenProgress(
        coins = prefs.getInt(KEY_COINS, 0),
        shifts = prefs.getInt(KEY_SHIFTS, 0),
        plates = prefs.getInt(KEY_PLATES, 0),
        perfects = prefs.getInt(KEY_PERFECTS, 0),
        unlockedLevels = prefs.getInt(KEY_UNLOCKED, 1).coerceAtLeast(1),
        lastLevel = prefs.getInt(KEY_LAST_LEVEL, 1).coerceAtLeast(1),
        tiers = StationTiers(
            burner = prefs.getInt(KEY_TIER_BURNER, 0),
            cooling = prefs.getInt(KEY_TIER_COOLING, 0),
            prep = prefs.getInt(KEY_TIER_PREP, 0)
        ),
        stars = readMap(KEY_STARS),
        bestScores = readMap(KEY_BEST),
        mastery = readMap(KEY_MASTERY)
    )

    fun saveProgress(progress: KitchenProgress) {
        prefs.edit()
            .putInt(KEY_COINS, progress.coins)
            .putInt(KEY_SHIFTS, progress.shifts)
            .putInt(KEY_PLATES, progress.plates)
            .putInt(KEY_PERFECTS, progress.perfects)
            .putInt(KEY_UNLOCKED, progress.unlockedLevels)
            .putInt(KEY_LAST_LEVEL, progress.lastLevel)
            .putInt(KEY_TIER_BURNER, progress.tiers.burner)
            .putInt(KEY_TIER_COOLING, progress.tiers.cooling)
            .putInt(KEY_TIER_PREP, progress.tiers.prep)
            .putString(KEY_STARS, encode(progress.stars))
            .putString(KEY_BEST, encode(progress.bestScores))
            .putString(KEY_MASTERY, encode(progress.mastery))
            .apply()
    }

    fun loadSettings(): GameSettings = GameSettings(
        leftHanded = prefs.getBoolean(KEY_LEFT_HANDED, false),
        numericTimer = prefs.getBoolean(KEY_NUMERIC_TIMER, false),
        highContrast = prefs.getBoolean(KEY_HIGH_CONTRAST, false),
        reduceMotion = prefs.getBoolean(KEY_REDUCE_MOTION, false)
    )

    fun saveSettings(settings: GameSettings) {
        prefs.edit()
            .putBoolean(KEY_LEFT_HANDED, settings.leftHanded)
            .putBoolean(KEY_NUMERIC_TIMER, settings.numericTimer)
            .putBoolean(KEY_HIGH_CONTRAST, settings.highContrast)
            .putBoolean(KEY_REDUCE_MOTION, settings.reduceMotion)
            .apply()
    }

    fun clearProgress() {
        prefs.edit()
            .remove(KEY_COINS)
            .remove(KEY_SHIFTS)
            .remove(KEY_PLATES)
            .remove(KEY_PERFECTS)
            .remove(KEY_UNLOCKED)
            .remove(KEY_LAST_LEVEL)
            .remove(KEY_TIER_BURNER)
            .remove(KEY_TIER_COOLING)
            .remove(KEY_TIER_PREP)
            .remove(KEY_STARS)
            .remove(KEY_BEST)
            .remove(KEY_MASTERY)
            .apply()
    }

    private fun readMap(key: String): Map<Int, Int> {
        val raw = prefs.getString(key, null).orEmpty()
        if (raw.isBlank()) return emptyMap()
        val result = LinkedHashMap<Int, Int>()
        raw.split(';').forEach { entry ->
            val parts = entry.split(':')
            if (parts.size == 2) {
                val k = parts[0].toIntOrNull()
                val v = parts[1].toIntOrNull()
                if (k != null && v != null) result[k] = v
            }
        }
        return result
    }

    private fun encode(map: Map<Int, Int>): String =
        map.entries.joinToString(";") { "${it.key}:${it.value}" }

    private companion object {
        const val STORE = "chili_kitchen_rush"
        const val KEY_COINS = "coins"
        const val KEY_SHIFTS = "shifts"
        const val KEY_PLATES = "plates"
        const val KEY_PERFECTS = "perfects"
        const val KEY_UNLOCKED = "unlocked_levels"
        const val KEY_LAST_LEVEL = "last_level"
        const val KEY_TIER_BURNER = "tier_burner"
        const val KEY_TIER_COOLING = "tier_cooling"
        const val KEY_TIER_PREP = "tier_prep"
        const val KEY_STARS = "stars"
        const val KEY_BEST = "best_scores"
        const val KEY_MASTERY = "mastery"
        const val KEY_LEFT_HANDED = "left_handed"
        const val KEY_NUMERIC_TIMER = "numeric_timer"
        const val KEY_HIGH_CONTRAST = "high_contrast"
        const val KEY_REDUCE_MOTION = "reduce_motion"
    }
}
