package com.mygdx.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.AssetLoader
import com.mygdx.game.impl.Scrollable
import com.mygdx.game.impl.Scrolled
import java.util.*

class CupboardDoors(private val cupboard: Cupboard) : Actor(), Scrollable {

    private val rand = Random()
    private val region = getRegion()
    private val region2 = getRegion()

    private val startY = 555f
    private val startX =  200
    var scroll = Scrolled(
            cupboard.scroll.getX(),
            startY,
            region.originalWidth,
            region.originalHeight,
            Scrolled.ScrollSpeed.LEVEL_1.value)

    private val randPositionX = Random(startX.toLong())

    private fun getRegion(): TextureAtlas.AtlasRegion{
        return when(rand.nextInt(3)){
            1 -> AssetLoader.openDoor
            2 -> AssetLoader.closeDoor
            else -> AssetLoader.openDoor
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        scroll.update(delta)
        if(scroll.isScrolledLeft){
            scroll.reset(cupboard.scroll.getX())

        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(region, scroll.getX(), scroll.getY(),
                scroll.width.toFloat(), scroll.height.toFloat())
    }

    override fun getX() = scroll.getX()
    override fun getY() = scroll.getY()

    override fun stopMove() {
        scroll.isStopMove = true
    }

    override fun runMove() {
        scroll.isStopMove = false
    }
}