package com.run.cookie.run.game.actors.main_menu_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.Animated
import com.run.cookie.run.game.api.AnimationType
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import com.run.cookie.run.game.managers.AudioManager
import com.run.cookie.run.game.managers.AudioManager.SoundApp
import com.run.cookie.run.game.managers.ScreenManager
import com.run.cookie.run.game.managers.ScreenManager.Param
import com.run.cookie.run.game.managers.ScreenManager.Screens.COMICS_SCREEN
import com.run.cookie.run.game.managers.ScreenManager.Screens.MAIN_MENU_SCREEN
import com.run.cookie.run.game.managers.VibrationManager

class CookieStoryIcon(manager : AssetManager, sound: GameActor) : GameActor(), Animated {

    private val texture = manager.get(Descriptors.menu)

    private var backgroundRegion = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON_MINI)
    private var iconRegion = texture.findRegion(Assets.MainMenuAtlas.COOKIE_STORY_ICON)

    private var centerX = 0f
    private var centerY = 0f

    init {
        width = backgroundRegion.originalWidth.toFloat()
        height = backgroundRegion.originalHeight.toFloat()
        x = Gdx.graphics.width - sound.x - width
        y = sound.y

        addClickListener()
    }

    private fun addClickListener(){
        addListener(object: ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                VibrationManager.vibrate()
                AudioManager.play(SoundApp.CLICK_SOUND)
                ScreenManager.setScreen(COMICS_SCREEN, Pair(Param.SCREEN_LINK, MAIN_MENU_SCREEN))
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    override fun act(delta: Float) {
        super.act(delta)

        centerX = x + (width / 2)
        centerY = y + (height / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = color
        batch.draw(backgroundRegion, x, y, width, height)

        val iconWidth = iconRegion.originalWidth.toFloat()
        val iconHeight = iconRegion.originalHeight.toFloat()
        batch.draw(iconRegion, centerX - (iconWidth / 2), centerY - (iconHeight/2), iconWidth, iconHeight)
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