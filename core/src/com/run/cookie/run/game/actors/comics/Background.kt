package com.run.cookie.run.game.actors.comics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.data.Descriptors
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.Assets

class Background(manager: AssetManager) : GameActor() {
    private val environmentAtlas = manager.get(Descriptors.environment)
    private val white = environmentAtlas.findRegion(Assets.EnvironmentAtlas.WHITE)

    init {
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height.toFloat()
        x = 0f
        y = 0f
    }

    override fun act(delta: Float) {
        super.act(delta)
        x = (Config.WIDTH_GAME - width) / 2f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(white, x,  y, width, height)
    }
}