package com.mygdx.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.AssetLoader

class Shadow : Actor() {
    private val shadow = AssetLoader.shadow

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(shadow, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }
}