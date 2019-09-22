package com.mygdx.game.actors

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.impl.Scrollable
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.impl.Scrolled

class Table(manager : AssetManager) : Actor(), Scrollable {
    private val texture = manager.get(Descriptors.environment)
    private val region = texture.findRegion(Assets.EnvironmentAtlas.TABLE)

    val worktopY = 50f

    private var scrollerBack = Scrolled(0f, 0f,
            region.originalWidth, region.originalHeight, Scrolled.ScrollSpeed.LEVEL_2.value)

    private var scrollerFront = Scrolled(scrollerBack.getTailX(), 0f,
            region.originalWidth, region.originalHeight, Scrolled.ScrollSpeed.LEVEL_2.value)
    init {

    }

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