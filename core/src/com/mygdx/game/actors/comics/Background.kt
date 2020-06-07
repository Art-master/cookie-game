package com.mygdx.game.actors.comics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.game.data.Descriptors
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets

class Background(manager: AssetManager) : GameActor() {
    private val atlas = manager.get(Descriptors.comics)
    private val background = atlas.findRegion(Assets.ComicsAtlas.BACKGROUND)
    private val environmentAtlas = manager.get(Descriptors.environment)
    private val white = environmentAtlas.findRegion(Assets.EnvironmentAtlas.WHITE)

    init {
        val screenWidth = Gdx.graphics.width
        width = background.originalWidth.toFloat()
        height = background.originalHeight.toFloat()
        x = (screenWidth - width) / 2f
        y = 0f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(white, 0f,  0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.draw(background, x,  y, width, height)
    }
}