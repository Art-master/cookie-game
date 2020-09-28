package com.mygdx.game.actors.game.cookie

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.mygdx.game.Config
import com.mygdx.game.api.GameActor
import com.mygdx.game.api.HorizontalScroll
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class JumpDust(manager: AssetManager, private val cookie: Cookie) : GameActor(), CookieLifecycle {

    private val texture = manager.get(Descriptors.cookie)
    private val regions = texture.findRegions(Assets.CookieAtlas.JUMP_DUST)
    private var jumpDustAnimation: Animation<TextureAtlas.AtlasRegion>? = null
    private val origWidth = regions.first().originalWidth.toFloat()
    private val origHeight = regions.first().originalHeight.toFloat()

    private var move = HorizontalScroll(0f, 0f, origWidth.toInt(), origHeight.toInt())

    init {
        width = origWidth
        height = origHeight
        y = cookie.startY - 20
        setColor(color.r, color.g, color.b, 0.5f)
    }

    override fun act(delta: Float) {
        super.act(delta)
        move.update(delta)
        x = move.getX()
        if (jumpDustAnimation != null && jumpDustAnimation!!.isAnimationFinished(cookie.runTime)) {
            jumpDustAnimation = null
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = color
        if (jumpDustAnimation != null) {
            val region = jumpDustAnimation?.getKeyFrame(cookie.runTime)
            batch.draw(region, x, y, 0f, 0f, width, height, scaleX, scaleY, rotation)
        }
    }

    override fun jumpEnd(isGround: Boolean) {
        if (isGround && jumpDustAnimation == null) {
            move.update(x = cookie.x - 20, y = cookie.startY - 20, speed = Config.ItemScrollSpeed.LEVEL_2)
            jumpDustAnimation = Animation(0.04f, regions)
        }
    }
}