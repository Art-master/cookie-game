package com.mygdx.game.actors.game_over_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.Config
import com.mygdx.game.api.Animated
import com.mygdx.game.api.AnimationType
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Descriptors

class GameOverMenuIcon(manager: AssetManager, private val posX: Float, private val posY: Float,
                       assetName: String, private val isLeft: Boolean = true) : GameActor(), Animated {

    private val texture = manager.get(Descriptors.menu)
    private val region = texture.findRegion(assetName)

    private val startX = if (isLeft) -region.originalWidth.toFloat()
    else Gdx.graphics.width + region.originalWidth.toFloat()

    init {
        this.x = startX
        this.y = posY
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()
        setOrigin(region.originalWidth / 2f, region.originalHeight / 2f)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = Config.SHADOW_ANIMATION_TIME_S

        val animation = when (type) {
            AnimationType.HIDE_FROM_SCENE -> {
                Actions.moveTo(startX, y, animDuration, Interpolation.exp10)
            }
            AnimationType.SHOW_ON_SCENE -> {
                Actions.moveTo(posX, posY, animDuration)
            }
            AnimationType.CLICK -> {
                val duration = 0.05f
                Actions.sequence(
                        Actions.scaleTo(1.03f, 1.03f, duration, Interpolation.smooth),
                        Actions.scaleTo(1f, 1f, duration, Interpolation.smooth))
            }
            else -> return
        }
        addAction(animation)
    }
}