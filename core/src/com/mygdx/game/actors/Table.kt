package com.mygdx.game.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.impl.Scrollable
import com.mygdx.game.data.AssetLoader
import com.mygdx.game.impl.Scrolled

class Table : Actor(), Scrollable {

    private val region = AssetLoader.table

    private var scrollerBack = Scrolled(0f, 0f,
            region.originalWidth, region.originalHeight, Scrolled.ScrollSpeed.LEVEL_2.value)

    private var scrollerFront = Scrolled(scrollerBack.getTailX(), 0f,
            region.originalWidth, region.originalHeight, Scrolled.ScrollSpeed.LEVEL_2.value)


    override fun act(delta: Float) {
        super.act(delta)
        scrollerBack.update(delta)
        scrollerFront.update(delta)

        if (scrollerBack.isScrolledLeft) {
            scrollerBack.reset(scrollerFront.getTailX())

        } else if (scrollerFront.isScrolledLeft) {
            scrollerFront.reset(scrollerBack.getTailX())
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(region,
                scrollerBack.getX(),
                scrollerBack.getY(),
                scrollerBack.width.toFloat(),
                scrollerBack.height.toFloat())

        batch.draw(region,
                scrollerFront.getX(),
                scrollerFront.getY(),
                scrollerFront.width.toFloat(),
                scrollerFront.height.toFloat())
    }

    override fun stopMove() {
        scrollerBack.isStopMove = true
        scrollerFront.isStopMove = true
    }

    override fun runMove() {
        scrollerBack.isStopMove = false
        scrollerFront.isStopMove = false
    }
}