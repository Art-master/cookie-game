package com.run.cookie.run.game.actors.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.api.Scrollable
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import com.run.cookie.run.game.api.HorizontalScroll

class Table(manager : AssetManager, yWorktop: Float) : GameActor(), Scrollable {
    private val texture = manager.get(Descriptors.environment)
    private val region = texture.findRegion(Assets.EnvironmentAtlas.TABLE)

    val worktopY = yWorktop

    private var scrollerBack = HorizontalScroll(0f, 0f,
            region.originalWidth, region.originalHeight, Config.ItemScrollSpeed.LEVEL_2)

    private var scrollerFront = HorizontalScroll(scrollerBack.getTailX(), 0f,
            region.originalWidth, region.originalHeight, Config.ItemScrollSpeed.LEVEL_2)

    override fun act(delta: Float) {
        super.act(delta)
        scrollerBack.act(delta)
        scrollerFront.act(delta)

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

    override fun updateSpeed() {
        scrollerBack.update()
        scrollerFront.update()
    }
}