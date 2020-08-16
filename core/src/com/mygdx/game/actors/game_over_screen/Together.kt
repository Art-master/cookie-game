package com.mygdx.game.actors.game_over_screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

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