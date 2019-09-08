package com.mygdx.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.impl.Scrollable
import com.mygdx.game.data.AssetLoader
import com.mygdx.game.impl.Scrolled

class Background : Actor(), Scrollable{

    private val texture = AssetLoader.backgroundTexture

    private var scrollerBack = Scrolled(0f, 0f,
            texture.width, texture.height, Scrolled.ScrollSpeed.LEVEL_1.value)

    private var scrollerFront = Scrolled(scrollerBack.getTailX(), 0f,
            texture.width, texture.height, Scrolled.ScrollSpeed.LEVEL_1.value)


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
        batch!!.color = color
        batch.draw(texture,
                scrollerBack.getX(),
                scrollerBack.getY(),
                scrollerBack.width.toFloat(),
                scrollerBack.height.toFloat())

        batch.draw(texture,
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
        scrollerFront.isStopMove = false
    }
}