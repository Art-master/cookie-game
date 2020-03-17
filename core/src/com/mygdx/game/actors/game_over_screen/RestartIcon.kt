package com.mygdx.game.actors.game_over_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.mygdx.game.Prefs
import com.mygdx.game.actors.Movable
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class RestartIcon(manager : AssetManager) : Actor(), Movable {

    private var prefs = Gdx.app.getPreferences(Prefs.NAME)

    private val texture = manager.get(Descriptors.menu)
    private val region = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON)
    private val replayButton = texture.findRegion(Assets.MainMenuAtlas.REPLAY_BUTTON)
    private val buttonPlay = texture.findRegion(Assets.MainMenuAtlas.BUTTON_PLAY)

    private var vibrationSettings = prefs.getBoolean(Prefs.VIBRATION, true)

    private var centerX = 0f
    private var centerY = 0f

    init {
        x = 100f
        y = 100f
        scaleX = 0.5f
        scaleY = 0.5f
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()

        addClickListener()
    }

    private fun addClickListener(){
        addListener(object: ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                vibrationSettings = vibrationSettings.not()
                prefs.flush()
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
        batch!!.color = Color.WHITE
        batch.draw(region, x, y, width, height)

        drawReplayIcon(batch)
        drawPlayIcon(batch)

    }

    private fun drawReplayIcon(batch: Batch){
        val iconWidth = replayButton.originalWidth.toFloat()
        val iconHeight = replayButton.originalHeight.toFloat()
        batch.draw(replayButton, centerX - (iconWidth / 2), centerY - (iconHeight/2), iconWidth, iconHeight)
    }

    private fun drawPlayIcon(batch: Batch){
        val iconWidth = buttonPlay.originalWidth.toFloat()
        val iconHeight = buttonPlay.originalHeight.toFloat()
        val x = centerX - (iconWidth / 2)
        val y = centerY - (iconHeight / 2)

        batch.draw(buttonPlay, x, y, x, y, iconWidth, iconHeight, 0.3f, 0.3f, 0f)
    }

    override fun move() {
        val animDuration = 0.5f
        val moveToOutside = Actions.moveTo(x, -Gdx.graphics.height.toFloat(), animDuration, Interpolation.exp10)
        addAction(moveToOutside)
    }
}