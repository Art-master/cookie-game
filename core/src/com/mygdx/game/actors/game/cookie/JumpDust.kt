package com.mygdx.game.actors.game.cookie

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class JumpDust(manager: AssetManager, private val cookie: Cookie) : GameActor(), CookieLifecycle {

    private val texture = manager.get(Descriptors.cookie)
    private val regions = texture.findRegions(Assets.CookieAtlas.JUMP_DUST)
    private var jumpDustAnimation: Animation<TextureAtlas.AtlasRegion>? = null
    private val origWidth = regions.first().originalWidth.toFloat()
    private val origHeight = regions.first().originalHeight.toFloat()

    init {
        width = origWidth
        height = origHeight
    }

    override fun act(delta: Float) {
        super.act(delta)
        x = cookie.x - 20
        y = cookie.ground - 20
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = color
        if (jumpDustAnimation != null && jumpDustAnimation!!.isAnimationFinished(cookie.runTime).not()) {
            val region = jumpDustAnimation?.getKeyFrame(cookie.runTime)
            batch.draw(region, x, y, 0f, 0f, width, height, scaleX, scaleY, rotation)
        }
    }

    override fun jumpEnd(isGround: Boolean) {
        if (isGround && jumpDustAnimation == null) jumpDustAnimation = Animation(0.04f, regions)
    }
}