package com.run.cookie.run.game.actors.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors

class Shadow(manager : AssetManager) : GameActor() {
    private val texture = manager.get(Descriptors.environment)
    private val region = texture.findRegion(Assets.EnvironmentAtlas.SHADOW)

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(region, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }
}