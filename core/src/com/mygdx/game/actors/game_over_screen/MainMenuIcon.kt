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
import com.mygdx.game.Config
import com.mygdx.game.Prefs
import com.mygdx.game.api.Animated
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class MainMenuIcon(manager : AssetManager) : Actor(), Animated {

    private var prefs = Gdx.app.getPreferences(Prefs.NAME)

    private val texture = manager.get(Descriptors.menu)
    private val region = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON)
    private val replayButton = texture.findRegion(Assets.MainMenuAtlas.CIRCLE)
    private val buttonPlay = texture.findRegion(Assets.MainMenuAtlas.MAIN_MENU_BUTTON)

    private var vibrationSettings = prefs.getBoolean(Prefs.VIBRATION, true)

    private var centerX = 0f
    private var centerY = 0f

    init {
        x = 1200f
        y = -Gdx.graphics.height.toFloat()
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()
        setOrigin(replayButton.originalWidth/2f, replayButton.originalHeight/2f )
        addClickListener()
        addPlayIconAnimationPulse()
    }

    private fun addPlayIconAnimationPulse(){
        val animDuration = 0.05f
        val scaleAnim1 = Actions.scaleTo(1.03f, 1.03f, animDuration, Interpolation.smooth)
        val scaleAnim2 = Actions.scaleTo(1f, 1f, animDuration, Interpolation.smooth)
        val scaleAnim3 = Actions.scaleTo(1.03f, 1.03f, animDuration, Interpolation.smooth)
        val scaleAnim4 = Actions.scaleTo(1f, 1f, animDuration, Interpolation.smooth)
        addAction(Actions.repeat(100, Actions.sequence(scaleAnim1, scaleAnim2, Actions.delay(animDuration),
                scaleAnim3, scaleAnim4, Actions.delay(3f))))
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
        val x = centerX - (iconWidth / 2)
        val y = centerY - (iconHeight / 2)

        batch.draw(replayButton, x, y, originX, originY, iconWidth, iconHeight,1f,1f, rotation)
    }

    private fun drawPlayIcon(batch: Batch){
        val iconWidth = buttonPlay.originalWidth.toFloat()
        val iconHeight = buttonPlay.originalHeight.toFloat()
        val x = centerX - (iconWidth / 2)
        val y = centerY - (iconHeight / 2)

        batch.draw(buttonPlay, x, y, originX, originY, iconWidth, iconHeight, scaleX, scaleY, 0f)
    }

    override fun animate(isReverse: Boolean, runAfter: Runnable) {
        val animDuration = Config.SHADOW_ANIMATION_TIME_S
        val moveToOutside = if(isReverse){
            Actions.moveTo(x, -Gdx.graphics.height.toFloat(), animDuration, Interpolation.exp10)
        }else{
            val y = 100f
            Actions.moveTo(x, y, animDuration)
        }
        addAction(moveToOutside)
    }
}