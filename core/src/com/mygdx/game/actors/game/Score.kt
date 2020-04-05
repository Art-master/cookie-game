package com.mygdx.game.actors.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.mygdx.game.Config
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets

class Score(manager : AssetManager) : GameActor() {
    private val generator = FreeTypeFontGenerator(Gdx.files.internal(Assets.Fonts.NAME))
    private var score: BitmapFont
    var scoreNum = 0
    private val symbol = 50
    private val paddingX = 300
    private val fontY = Gdx.graphics.height - 50f

    init {
        val offSetShadow = 5

        val param = FreeTypeFontGenerator.FreeTypeFontParameter()
        param.color = Color.YELLOW
        param.shadowOffsetX = -offSetShadow
        param.shadowOffsetY = offSetShadow
        param.spaceX = 15
        param.size = 100
        score = generator.generateFont(param)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val fontX = Config.WIDTH_GAME - (symbol * scoreNum.toString().length) - paddingX
        score.draw(batch, scoreNum.toString(), fontX, fontY)
    }
}