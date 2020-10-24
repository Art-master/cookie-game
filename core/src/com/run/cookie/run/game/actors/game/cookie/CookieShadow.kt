package com.run.cookie.run.game.actors.game.cookie

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors

class CookieShadow(manager : AssetManager, private val cookie: Cookie): GameActor() {

    private val texture = manager.get(Descriptors.environment)
    private val region = texture.findRegion(Assets.EnvironmentAtlas.CIRCLE_SHADOW)
    private val origWidth = region.originalWidth.toFloat()
    private val origHeight = region.originalHeight.toFloat()

    init {
        width = origWidth
        height = origHeight
    }

    override fun act(delta: Float) {
        super.act(delta)
        setShadow()
        isVisible = cookie.isVisible
    }

    private fun setShadow(){
        setShadowOpacity()
        setShadowSize()
    }

    private fun setShadowSize(){
        val maxSizePoint = 400
        val percent = 100 - ((cookie.y - cookie.startY ) / (maxSizePoint / 100))
        if(percent > 0 && percent <= 100){
            width = region.originalWidth / (100 / percent)
            height = region.originalHeight / (100 / percent)
        }
    }

    private fun setShadowOpacity(){
        val invisiblePoint = 100
        val percent = 100 - ((cookie.y - cookie.startY ) / (invisiblePoint / 100))
        val alpha = when {
            percent / 100 > 1 -> 1f
            percent / 100 < 0 -> 0f
            else -> percent / 100
        }
        color.a = alpha
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val x = if(cookie.state == Cookie.State.WIN) cookie.x + 50 else cookie.x
        val y = cookie.startY - 20
        batch!!.color = color
        if(cookie.state == Cookie.State.SLIP) return
        batch.draw(region, x, y, 0f, 0f,  width, height, scaleX, scaleY, rotation)
    }
}