package com.mygdx.game.actors.main_menu_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.mygdx.game.Prefs
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class VibrationIcon(manager : AssetManager, sound: Actor) : Actor() {
    private var prefs = Gdx.app.getPreferences(Prefs.NAME)

    private val texture = manager.get(Descriptors.menu)
    private val region = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON)
    private val vibrationOnRegion = texture.findRegion(Assets.MainMenuAtlas.VIBR_ON)
    private val vibrationOffRegion = texture.findRegion(Assets.MainMenuAtlas.VIBR_OFF)
    private var vibrationIcon = vibrationOnRegion

    private var vibrationSettings = prefs.getBoolean(Prefs.VIBRATION, true)

    private var centerX = 0f
    private var centerY = 0f

    init {
        val soundPosX = sound.x + sound.width
        x = soundPosX + 100f
        y = sound.y
        width = region.originalWidth/3f
        height = region.originalHeight/3f

        addClickListener()
        changeVibrationIcon()
    }

    private fun addClickListener(){
        addListener(object: ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                vibrationSettings = vibrationSettings.not()
                prefs.putBoolean(Prefs.VIBRATION, vibrationSettings)
                prefs.flush()
                changeVibrationIcon()
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    private fun changeVibrationIcon() {
        vibrationIcon = if(vibrationSettings) vibrationOnRegion else vibrationOffRegion
    }

    override fun act(delta: Float) {
        super.act(delta)

        centerX = x + (width / 2)
        centerY = y + (height / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(region, x, y, width, height)

        val iconWidth = vibrationIcon.originalWidth.toFloat()
        val iconHeight = vibrationIcon.originalHeight.toFloat()
        batch.draw(vibrationIcon, centerX - (iconWidth / 2), centerY - (iconHeight/2), iconWidth, iconHeight)
    }
}