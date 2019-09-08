package com.mygdx.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.AssetLoader

class Sky(private val window: Window) : Actor() {
    private val skyTexture = AssetLoader.blueSky

    private val divX = 170
    private val divY = 180

    override fun act(delta: Float) {
        super.act(delta)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(skyTexture,
                window.scroll.getX() + divX, window.scroll.getY() + divY,
                window.scroll.width -300f, window.scroll.height -300f)
    }
}