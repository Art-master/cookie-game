package com.run.cookie.run.game.actors.game_over_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.Prefs
import com.run.cookie.run.game.api.Animated
import com.run.cookie.run.game.api.AnimationType
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.FontParam

class Scores(manager: AssetManager, private val currentScoreNum: Int = 0) : GameActor(), Animated {

    private var score: BitmapFont = manager.get(FontParam.CURRENT_SCORE.fontName)
    private var scoreBest: BitmapFont = manager.get(FontParam.BEST_SCORE.fontName)
    var bestScoreNum: Int = 0

    private val symbol = 50

    private val prefs = Gdx.app.getPreferences(Prefs.NAME)

    init {
        initScoreNum()
        buildBestScoreAnimation()
        color.a = 0f

        x = 30f
    }

    private fun buildBestScoreAnimation() {
        val step = 5
        val count = (bestScoreNum / step) + 1
        val startValue = bestScoreNum
        bestScoreNum = 0
        addAction(Actions.repeat(count,
                Actions.delay(0.05f, Actions.run {
                    bestScoreNum += if(bestScoreNum + step <= startValue) step
                    else startValue % step
                })))
    }

    private fun initScoreNum() {
        bestScoreNum = prefs.getInteger(Prefs.BEST_SCORE)
        if (bestScoreNum == 0) bestScoreNum = currentScoreNum
        if (currentScoreNum > bestScoreNum) {
            bestScoreNum = currentScoreNum
        }
        prefs.putInteger(Prefs.BEST_SCORE, bestScoreNum).flush()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        drawCurrentScore(batch!!)
        drawBestScore(batch)
    }

    private fun drawCurrentScore(batch: Batch) {
        val centerScreenY = 480f
        score.color.a = color.a
        score.draw(batch, "SCORE: $currentScoreNum", x, centerScreenY)
    }

    private fun drawBestScore(batch: Batch) {
        val centerScreenY = 430f
        scoreBest.color.a = color.a
        scoreBest.draw(batch, "BEST: $bestScoreNum", x, centerScreenY - (symbol * 2))
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = Config.SHADOW_ANIMATION_TIME_S
        val animation = when (type) {
            AnimationType.HIDE_FROM_SCENE -> {
                val invisible = 0f
                Actions.alpha(invisible, animDuration, Interpolation.exp10)
            }
            AnimationType.SHOW_ON_SCENE -> {
                val visible = 1f
                Actions.alpha(visible, animDuration, Interpolation.exp10)
            }
            else -> return
        }

        val run = Actions.run(runAfter)
        val sequence = Actions.sequence(animation, run)
        addAction(sequence)
    }
}