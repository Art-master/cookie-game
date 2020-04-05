package com.mygdx.game.actors.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.game.api.GameActor
import com.mygdx.game.api.Hideable
import com.mygdx.game.beans.Position
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.api.Scrollable

class City(manager : AssetManager, private val window : Window) : GameActor(), Scrollable {
    private val texture = manager.get(Descriptors.environment)
    private val cityRegion = texture.findRegion(Assets.EnvironmentAtlas.CITY)

    private val posCity = Position(
            0f,
            window.getWindowsillY(),
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