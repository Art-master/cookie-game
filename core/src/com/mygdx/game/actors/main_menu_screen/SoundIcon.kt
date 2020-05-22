package com.mygdx.game.actors.main_menu_screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import com.mygdx.game.Config
import com.mygdx.game.api.Animated
import com.mygdx.game.api.AnimationType
import com.mygdx.game.api.GameActor
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.AudioManager.MusicApp
import com.mygdx.game.managers.AudioManager.Sound


class SoundIcon(manager : AssetManager, sound: GameActor) : GameActor(), Animated {
    private val texture = manager.get(Descriptors.menu)
    private val soundRegion = texture.findRegion(Assets.MainMenuAtlas.SOUND_ICON)
    private var backgroundRegion = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON_MINI)
    private var backgroundRegion2 = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON_MINI_2)
    private var region = backgroundRegion
    private var soundIcon = soundRegion

    private var centerX = 0f
    private var centerY = 0f

    init {
        isVibrating = true
        x = sound.right + 100f
        y = sound.y
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()

        addClickListener()
        changeBackground()
    }

    private fun addClickListener(){
        addListener(object: ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                AudioManager.switchSoundSetting()
                AudioManager.play(MusicApp.MAIN_MENU_MUSIC)
                AudioManager.play(Sound.CLICK_SOUND)
                changeBackground()
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    private fun changeBackground(){
        region = if(AudioManager.isSoundEnable) backgroundRegion else backgroundRegion2
    }

    override fun act(delta: Float) {
        super.act(delta)

        centerX = x + (width / 2)
        centerY = y + (height / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(region, x, y, width, height)

        val iconWidth = soundIcon.originalWidth.toFloat()
        val iconHeight = soundIcon.originalHeight.toFloat()
        batch.draw(soundIcon, centerX - (iconWidth / 2), centerY - (iconHeight / 2), iconWidth, iconHeight)
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