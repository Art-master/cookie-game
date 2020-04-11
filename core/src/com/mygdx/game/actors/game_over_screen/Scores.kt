package com.mygdx.game.actors.game_over_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.mygdx.game.Config
import com.mygdx.game.Prefs
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets

class Scores(private val currentScoreNum: Int) : GameActor() {
    private val generator = FreeTypeFontGenerator(Gdx.files.internal(Assets.Fonts.NAME))

    private var currentScore: BitmapFont
    private var bestScore: BitmapFont
    private var bestScoreNum = 4

    private val symbol = 50

    private val prefs = Gdx.app.getPreferences(Prefs.NAME)

    init {
        val offSetShadow = 5

        val param = FreeTypeFontGenerator.FreeTypeFontParameter()
        param.color = Color.YELLOW
        param.shadowOffsetX = -offSetShadow
        param.shadowOffsetY = offSetShadow
        param.spaceX = 15
        param.size = 150
        currentScore = generator.generateFont(param)
        bestScore = generator.generateFont(param)
        initScoreNum()
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
        currentScore.draw(batch, "Current : $currentScoreNum", fontX, centerScreenY)
    }

    private fun drawBestScore(batch: Batch){
        val centerScreenX = Config.WIDTH_GAME / 2
        val centerScreenY = 700f
        val fontX = centerScreenX - (symbol * bestScoreNum.toString().length / 2) - 300
        bestScore.draw(batch, "Best : $bestScoreNum", fontX, centerScreenY - (symbol * 2))
    }
}