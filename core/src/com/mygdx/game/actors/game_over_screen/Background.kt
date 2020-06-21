package com.mygdx.game.actors.game_over_screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.game.data.Descriptors
import com.badlogic.gdx.graphics.Color
import com.mygdx.game.api.GameActor

class Background(manager : AssetManager) : GameActor() {
    private val backgroundTexture = manager.get(Descriptors.gameOverBackground)

    init {
        width = backgroundTexture.width.toFloat()
        height = backgroundTexture.height.toFloat()
        x = 0f
        y = 0f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(backgroundTexture, x, y, width, height)
    }
}