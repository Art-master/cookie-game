package com.mygdx.game.actors.main_menu_screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import com.mygdx.game.Config
import com.mygdx.game.actors.Animated
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.AudioManager.MusicApp
import com.mygdx.game.managers.AudioManager.Sounds
import com.mygdx.game.managers.VibrationManager


class SoundIcon(manager : AssetManager) : Actor(), Animated {
    private val texture = manager.get(Descriptors.menu)
    private val region = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON)
    private val soundOnRegion = texture.findRegion(Assets.MainMenuAtlas.SOUND_ON)
    private val soundOffRegion = texture.findRegion(Assets.MainMenuAtlas.SOUND_OFF)
    private var soundIcon = soundOnRegion

    private var centerX = 0f
    private var centerY = 0f

    init {
        x = 100f
        y = -Gdx.graphics.height.toFloat()
        width = region.originalWidth/3f
        height = region.originalHeight/3f

        addClickListener()
        changeSoundIcon()
    }

    private fun addClickListener(){
        addListener(object: ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                VibrationManager.vibrate()
                AudioManager.switchSoundSetting()
                if(AudioManager.isMusicEnable) AudioManager.play(MusicApp.MAIN_MENU_MUSIC)
                AudioManager.play(Sounds.CLICK_SOUND)
                changeSoundIcon()
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    private fun changeSoundIcon(){
        soundIcon = if(AudioManager.isMusicEnable) soundOnRegion else soundOffRegion
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

    override fun animate(isReverse: Boolean, runAfter: Runnable) {
        val animDuration = Config.BUTTONS_ANIMATION_TIME_S / 2
        val moveToOutside = if(isReverse){
            moveTo(x, -Gdx.graphics.height.toFloat(), animDuration, Interpolation.exp10)
        }else{
            val y = 50f
            moveTo(x, y, animDuration)
        }
        addAction(moveToOutside)
    }
}