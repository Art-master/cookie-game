package com.mygdx.game.actors.game_over_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.Config
import com.mygdx.game.Prefs
import com.mygdx.game.api.Animated
import com.mygdx.game.api.AnimationType
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets

class Scores(private val currentScoreNum: Int = 0) : GameActor(), Animated {

    private val generator = FreeTypeFontGenerator(Gdx.files.internal(Assets.Fonts.SCORE_FONT))

    private var score: BitmapFont
    private var bestScoreNum: Int = 0

    private val symbol = 50

    private val prefs = Gdx.app.getPreferences(Prefs.NAME)

    init {
        val offSetShadow = 5

        val param = FreeTypeFontGenerator.FreeTypeFontParameter()
        param.color = Color.valueOf("#ff5742")
        param.shadowOffsetX = -offSetShadow
        param.shadowOffsetY = offSetShadow
        param.spaceX = 15
        param.size = 140
        score = generator.generateFont(param)
        initScoreNum()
        color.a = 0f
    }

    private fun initScoreNum(){
        bestScoreNum = prefs.getInteger(Prefs.BEST_SCORE)
        if(bestScoreNum == 0) bestScoreNum = currentScoreNum
        if(currentScoreNum > bestScoreNum){
            bestScoreNum = currentScoreNum
        }
        prefs.putInteger(Prefs.BEST_SCORE, bestScoreNum).flush()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        drawCurrentScore(batch!!)
        drawBestScore(batch)
    }

    private fun drawCurrentScore(batch: Batch){
        val centerScreenX = Config.WIDTH_GAME / 2
        val centerScreenY = 800f
        val fontX = centerScreenX - (symbol * currentScoreNum.toString().length / 2) - 300
        score.color.a = color.a
        score.draw(batch, "SCORE: $currentScoreNum", fontX, centerScreenY)
    }

    private fun drawBestScore(batch: Batch){
        val centerScreenX = Config.WIDTH_GAME / 2
        val centerScreenY = 700f
        val fontX = centerScreenX - (symbol * bestScoreNum.toString().length / 2) - 300
        score.color.a = color.a
        score.draw(batch, "BEST: $bestScoreNum", fontX, centerScreenY - (symbol * 2))
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = Config.SHADOW_ANIMATION_TIME_S
        val animation = when(type){
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