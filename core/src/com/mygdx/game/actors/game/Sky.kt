package com.mygdx.game.actors.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class Sky(manager : AssetManager, private val window: Window) : Actor() {
    private val texture = manager.get(Descriptors.environment)
    private val region = texture.findRegion(Assets.EnvironmentAtlas.BLUE_SKY)

    private val divX = 170
    private val divY = 180

    override fun act(delta: Float) {
        super.act(delta)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(region,
                window.scroll.getX() + divX, window.scroll.getY() + divY,
                window.scroll.width -300f, window.scroll.height -300f)
    }
}