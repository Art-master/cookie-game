package com.mygdx.game.actors.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.game.Config
import com.mygdx.game.api.GameActor
import com.mygdx.game.api.Scrollable
import com.mygdx.game.data.Descriptors
import com.mygdx.game.api.Scrolled

class Background(manager : AssetManager) : GameActor(), Scrollable{

    private val texture = manager.get(Descriptors.background)

    private var scrollerBack = Scrolled(0f, 0f,
            texture.width, texture.height, Config.ItemScrollSpeed.LEVEL_1)

    private var scrollerFront = Scrolled(scrollerBack.getTailX(), 0f,
            texture.width, texture.height, Config.ItemScrollSpeed.LEVEL_1)


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
        scrollerBack.isStopMove = false
    }

    override fun updateSpeed() {
        scrollerBack.update()
        scrollerFront.update()
    }
}