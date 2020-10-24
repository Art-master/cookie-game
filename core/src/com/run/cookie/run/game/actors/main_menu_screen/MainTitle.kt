package com.run.cookie.run.game.actors.main_menu_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.Animated
import com.run.cookie.run.game.api.AnimationType
import com.run.cookie.run.game.api.GameActor

class MainTitle(manager : AssetManager) : GameActor(), Animated {
    private val texture = manager.get(Descriptors.menu)
    private val region = texture.findRegion(Assets.MainMenuAtlas.TITLE)

    private var centerX = 0f
    private var centerY = 0f


    init {
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()
        x = (Config.WIDTH_GAME / 2) - width / 2
        y = Gdx.graphics.height.toFloat()
    }

    override fun act(delta: Float) {
        super.act(delta)

        centerX = x + (width / 2)
        centerY = y + (height / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(region, x, y, width, height)
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = Config.BUTTONS_ANIMATION_TIME_S / 2

        val animation = when(type){
            AnimationType.HIDE_FROM_SCENE -> {
                moveTo(x, Gdx.graphics.height.toFloat(), animDuration, Interpolation.exp10)
            }
            AnimationType.SHOW_ON_SCENE -> {
                val y = (Config.HEIGHT_GAME - height - 50)
                moveTo(x, y, animDuration, Interpolation.exp10)
            }
            else -> return
        }
        addAction(animation)
    }
}