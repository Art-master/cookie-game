package com.mygdx.game.actors.loading_progress

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.badlogic.gdx.graphics.Color
import com.mygdx.game.api.GameActor

class Background(manager : AssetManager) : GameActor() {
    private val progressAtlas = manager.get(Descriptors.progressBar)
    private val whiteSquareRegion = progressAtlas.findRegion(Assets.ProgressAtlas.WHITE_SQUARE)

    init {
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height.toFloat()
        x = 0f
        y = 0f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        drawBackground(batch)
    }

    private fun drawBackground(batch: Batch){
        batch.draw(whiteSquareRegion, x, y, width, height)
    }
}