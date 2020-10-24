package com.run.cookie.run.game.actors.game_over_screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors

class Together(manager : AssetManager) : GameActor() {
    private val texture = manager.get(Descriptors.cookie)
    private val region = texture.findRegion(Assets.CookieAtlas.TOGETHER)

    init {
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()
        x = 740f
        y = 290f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(region, x, y, width, height)
    }
}