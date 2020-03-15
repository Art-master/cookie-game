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
import com.mygdx.game.Prefs


class SoundIcon(manager : AssetManager) : Actor() {
    private var prefs = Gdx.app.getPreferences(Prefs.NAME)

    private val texture = manager.get(Descriptors.menu)
    private val region = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON)
    private val soundOnRegion = texture.findRegion(Assets.MainMenuAtlas.SOUND_ON)
    private val soundOffRegion = texture.findRegion(Assets.MainMenuAtlas.SOUND_OFF)
    private var soundIcon = soundOnRegion

    private var soundSettings = prefs.getBoolean(Prefs.SOUND, true)

    private var centerX = 0f
    private var centerY = 0f

    init {
        x = 100f
        y = 50f
        width = region.originalWidth/3f
        height = region.originalHeight/3f

        addClickListener()
        changeSoundIcon()
    }

    private fun addClickListener(){
        addListener(object: ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                soundSettings = soundSettings.not()
                prefs.putBoolean(Prefs.SOUND, soundSettings)
                prefs.flush()
                changeSoundIcon()
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    private fun changeSoundIcon(){
        soundIcon = if(soundSettings) soundOnRegion else soundOffRegion
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
}