package com.run.cookie.run.game.actors.main_menu_screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.Animated
import com.run.cookie.run.game.api.AnimationType
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.managers.AudioManager


class SoundIcon(manager : AssetManager, sound: GameActor) : GameActor(), Animated {
    private val texture = manager.get(Descriptors.menu)
    private val soundRegion = texture.findRegion(Assets.MainMenuAtlas.SOUND_ICON)
    private var backgroundRegion = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON_MINI)
    private var backgroundRegion2 = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON_MINI_CRASH)
    private var disableLineRegion = texture.findRegion(Assets.MainMenuAtlas.DISABLE_LINE)
    private var background = backgroundRegion
    private var soundIcon = soundRegion

    private var centerX = 0f
    private var centerY = 0f

    init {
        isVibrating = true
        x = sound.right + 100f
        y = sound.y
        width = background.originalWidth.toFloat()
        height = background.originalHeight.toFloat()
        changeBackground()
    }

    fun changeBackground(){
        background = if(AudioManager.isSoundEnable) {
            color.a = 1f
            backgroundRegion
        } else {
            color.a = 0.5f
            backgroundRegion2
        }
    }

    override fun act(delta: Float) {
        super.act(delta)

        centerX = x + (width / 2)
        centerY = y + (height / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = color
        batch.draw(background, x, y, width, height)


        val iconWidth = soundIcon.originalWidth.toFloat()
        val iconHeight = soundIcon.originalHeight.toFloat()

        batch.color = color
        batch.draw(soundIcon, centerX - (iconWidth / 2), centerY - (iconHeight / 2), iconWidth, iconHeight)
        drawDisableLine(batch)
    }

    private fun drawDisableLine(batch: Batch){
        if(AudioManager.isSoundEnable.not()){
            val iconWidth = disableLineRegion.originalWidth.toFloat()
            val iconHeight = disableLineRegion.originalHeight.toFloat()
            batch.draw(disableLineRegion, centerX - (iconWidth / 2), centerY - (iconHeight / 2), iconWidth, iconHeight)
        }
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = Config.BUTTONS_ANIMATION_TIME_S / 2
        val action = when(type) {
            AnimationType.HIDE_FROM_SCENE -> {
                moveTo(x, -Gdx.graphics.height.toFloat(), animDuration, Interpolation.exp10)
            }
            AnimationType.SHOW_ON_SCENE -> {
                val y = 50f
                moveTo(x, y, animDuration)
            }
            else -> return
        }
        addAction(action)
    }
}