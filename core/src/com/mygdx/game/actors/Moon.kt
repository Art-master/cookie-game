package com.mygdx.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.impl.Hideable
import com.mygdx.game.beans.Position
import com.mygdx.game.data.AssetLoader

class Moon(private val window : Window) : Actor() {

    private val moonRegion = AssetLoader.moon

    private val posMoon = Position(
            (Gdx.graphics.width/2).toFloat(),
            window.scroll.getY() + 220,
            moonRegion.originalWidth.toFloat(),
            moonRegion.originalHeight.toFloat())

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val windowPos = Position(
                window.scroll.getX() + 120,
                window.scroll.getY(),
                window.scroll.width -300f,
                window.scroll.height.toFloat())

        val moonPos = Hideable(posMoon, windowPos)
        batch!!.draw(
                moonRegion.texture,
                moonPos.getX(),
                moonPos.getY(),
                moonPos.getX(),
                moonPos.getY(),
                moonPos.getDrawWidth(),
                moonPos.getDrawHeight(),
                1f,
                1f,
                0f,
                moonRegion.regionX + moonPos.getTextureX().toInt(),
                moonRegion.regionY + moonPos.getTextureY().toInt(),
                moonPos.getDrawWidth().toInt(),
                moonPos.getDrawHeight().toInt(),
                false,
                false)
    }
}