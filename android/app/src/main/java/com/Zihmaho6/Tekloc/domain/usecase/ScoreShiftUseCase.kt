package com.Zihmaho6.Tekloc.domain.usecase

import com.Zihmaho6.Tekloc.core.config.GameConfig
import com.Zihmaho6.Tekloc.domain.model.LevelSpec
import com.Zihmaho6.Tekloc.domain.model.ShiftOutcome
import com.Zihmaho6.Tekloc.domain.model.ShiftResult

class ScoreShiftUseCase {

    operator fun invoke(
        spec: LevelSpec,
        outcome: ShiftOutcome,
        plates: Int,
        perfects: Int,
        bestCombo: Int,
        coins: Int,
        score: Int,
        heartsLeft: Int,
        previousBest: Int
    ): ShiftResult {
        var stars = 0
        if (outcome == ShiftOutcome.QUOTA_MET) {
            stars = 1
            if (perfects * 2 >= spec.quota) stars = 2
            if (heartsLeft == GameConfig.MAX_HEARTS) stars = 3
        }
        return ShiftResult(
            level = spec.index,
            outcome = outcome,
            plates = plates,
            quota = spec.quota,
            perfects = perfects,
            bestCombo = bestCombo,
            coins = coins,
            score = score,
            stars = stars,
            newRecord = score > previousBest && score > 0
        )
    }
}
