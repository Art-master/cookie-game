package com.run.cookie.run.game.actors.game.cookie

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.api.HorizontalScroll
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors

class FallDust(manager: AssetManager, private val cookie: Cookie) : GameActor(), CookieLifecycle {

    private val texture = manager.get(Descriptors.cookie)
    private val regions = texture.findRegions(Assets.CookieAtlas.FALL_DUST)
    private var dustAnimation: Animation<TextureAtlas.AtlasRegion>? = null
    private val origWidth = regions.first().originalWidth.toFloat()
    private val origHeight = regions.first().originalHeight.toFloat()

    private var move = HorizontalScroll(0f, 0f, origWidth.toInt(), origHeight.toInt())

    private var runTime = 0f

    init {
        width = origWidth
        height = origHeight
        y = cookie.startY - 20
        setColor(color.r, color.g, color.b, 0.7f)
    }

    override fun act(delta: Float) {
        super.act(delta)

        move.act(delta)
        x = move.getX()
        if (dustAnimation != null && dustAnimation!!.isAnimationFinished(runTime)) {
            dustAnimation = null
        } else runTime += delta
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = color
        if (dustAnimation != null) {
            val region = dustAnimation?.getKeyFrame(runTime)
            batch.draw(region, x, y, 0f, 0f, width, height, scaleX, scaleY, rotation)
        }
    }

    override fun stumbled() {
        if (dustAnimation == null) {
            runTime = 0f
            move.update(x = cookie.x, y = cookie.startY - 20, speed = Config.ItemScrollSpeed.LEVEL_2)
            dustAnimation = Animation(0.04f, regions)
        }
    }
}