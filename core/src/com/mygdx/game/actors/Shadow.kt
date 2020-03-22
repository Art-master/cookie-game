package com.mygdx.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.Config.SHADOW_ANIMATION_TIME_S
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class Shadow(manager : AssetManager) : Actor(), Animated {
    private val texture = manager.get(Descriptors.menu)
    private val region = texture.findRegion(Assets.MainMenuAtlas.BLACK_SQUARE)

    init {
        x = 0f
        y = 0f
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height.toFloat()

        val color = color
        setColor(color.r, color.g, color.b, 0f)
        touchable = Touchable.disabled
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE

        batch.color = color
        batch.draw(region, x, y, width, height)
        batch.setColor(color.r, color.g, color.b, 1f)
    }

    override fun animate(isReverse: Boolean, runAfter: Runnable) {
        val animDuration = SHADOW_ANIMATION_TIME_S
        val moveToOutside = if(isReverse){
            Actions.alpha(1f, animDuration)
        }else{
            val color = color
            setColor(color.r, color.g, color.b, 1f)
            Actions.alpha(0f, animDuration)
        }
        val run = Actions.run(runAfter)
        val sequence = Actions.sequence(moveToOutside, run)
        addAction(sequence)
    }
}