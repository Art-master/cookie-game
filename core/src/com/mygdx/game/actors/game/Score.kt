package com.mygdx.game.actors.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.mygdx.game.Config
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.FontParam

class Score(manager : AssetManager) : GameActor() {
    private var score: BitmapFont = manager.get(FontParam.SCORE.fontName)
    var scoreNum = 0
    private val symbol = 50
    private val paddingX = 300
    private val fontY = Gdx.graphics.height - 50f

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val fontX = Config.WIDTH_GAME - (symbol * scoreNum.toString().length) - paddingX
        score.draw(batch, scoreNum.toString(), fontX, fontY)
    }
}