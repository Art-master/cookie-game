package com.mygdx.game.actors.main_menu_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.mygdx.game.Config
import com.mygdx.game.api.Animated
import com.mygdx.game.api.AnimationType
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.AudioManager.Sound
import com.mygdx.game.managers.VibrationManager

class VibrationIcon(manager : AssetManager, sound: GameActor) : GameActor(), Animated {

    private val texture = manager.get(Descriptors.menu)

    private var backgroundRegion = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON_MINI)
    private var backgroundRegion2 = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON_MINI_2)
    private var disableLineRegion = texture.findRegion(Assets.MainMenuAtlas.DISABLE_LINE)
    private var background = backgroundRegion

    private var vibrationIcon = texture.findRegion(Assets.MainMenuAtlas.VIBRATION_ICON)

    private var centerX = 0f
    private var centerY = 0f

    init {
        width = background.originalWidth.toFloat()
        height = background.originalHeight.toFloat()
        x = Gdx.graphics.width - sound.x - width
        y = sound.y

        addClickListener()
        changeBackground()
    }

    private fun addClickListener(){
        addListener(object: ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                VibrationManager.switchVibrationSetting()
                VibrationManager.vibrate()
                AudioManager.play(Sound.CLICK_SOUND)
                changeBackground()
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    private fun changeBackground(){
        background = if(VibrationManager.isVibrationEnable) {
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

        val iconWidth = vibrationIcon.originalWidth.toFloat()
        val iconHeight = vibrationIcon.originalHeight.toFloat()
        batch.draw(vibrationIcon, centerX - (iconWidth / 2), centerY - (iconHeight/2), iconWidth, iconHeight)
        drawDisableLine(batch)
    }

    private fun drawDisableLine(batch: Batch){
        if(VibrationManager.isVibrationEnable.not()){
            val iconWidth = disableLineRegion.originalWidth.toFloat()
            val iconHeight = disableLineRegion.originalHeight.toFloat()
            batch.draw(disableLineRegion, centerX - (iconWidth / 2), centerY - (iconHeight / 2), iconWidth, iconHeight)
        }
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = Config.BUTTONS_ANIMATION_TIME_S / 2
        val action = when(type) {
            AnimationType.HIDE_FROM_SCENE -> {
                Actions.moveTo(x, -Gdx.graphics.height.toFloat(), animDuration, Interpolation.exp10)
            }
            AnimationType.SHOW_ON_SCENE -> {
                val y = 50f
                Actions.moveTo(x, y, animDuration)
            }
            else -> return
        }
        addAction(action)
    }
}