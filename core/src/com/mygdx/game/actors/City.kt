package com.mygdx.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.impl.Hideable
import com.mygdx.game.beans.Position
import com.mygdx.game.data.AssetLoader
import com.mygdx.game.impl.Scrollable

class City(private val window : Window) : Actor(), Scrollable {

    private val cityRegion = AssetLoader.city

    private val posCity = Position(
            0f,
            window.scroll.getY() + window.getWindowsillY(),
            cityRegion.originalWidth.toFloat(),
            cityRegion.originalHeight.toFloat())

    override fun draw(batch: Batch?, parentAlpha: Float) {

        val windowPos = Position(
                window.scroll.getX() + 110,
                window.scroll.getY(),
                window.scroll.width -280f,
                window.scroll.height -560f)

        val cityHidePos = Hideable(posCity, windowPos)

        batch!!.draw(
                cityRegion.texture,
                cityHidePos.getX(),
                cityHidePos.getY(),
                cityHidePos.getX(),
                cityHidePos.getY(),
                cityHidePos.getDrawWidth(),
                cityHidePos.getDrawHeight(),
                1f,
                1f,
                0f,
                cityRegion.regionX + cityHidePos.getTextureX().toInt(),
                cityRegion.regionY + cityHidePos.getTextureY().toInt(),
                cityHidePos.getDrawWidth().toInt(),
                cityHidePos.getDrawHeight().toInt(),
                false,
                false)
    }

    override fun runMove() {
    }

    override fun stopMove() {
    }
}