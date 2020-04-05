package com.mygdx.game.actors.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class Shadow(manager : AssetManager) : GameActor() {
    private val texture = manager.get(Descriptors.environment)
    private val region = texture.findRegion(Assets.EnvironmentAtlas.SHADOW)

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(region, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }
}