package com.mygdx.game.actors.main_menu_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.mygdx.game.Config
import com.mygdx.game.ScreenManager
import com.mygdx.game.actors.Movable
import com.mygdx.game.screens.GameScreen

class PlayButton(manager : AssetManager) : Actor(), Movable {

    private val texture = manager.get(Descriptors.menu)
    private val region = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON)
    private var playIcon = texture.findRegion(Assets.MainMenuAtlas.BUTTON_PLAY)

    private var centerX = 0f
    private var centerY = 0f


    init {
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()
        x = (Config.widthGame / 2) - width / 2
        y = (Config.heightGame / 6)
        addPlayIconAnimationPulse()
    }

    private fun addPlayIconAnimationPulse(){
        val animDuration = 0.05f
        val scaleAnim1 = scaleTo(1.01f, 1.01f, animDuration, Interpolation.smooth)
        val scaleAnim2 = scaleTo(1f, 1f, animDuration, Interpolation.smooth)
        val scaleAnim3 = scaleTo(1.01f, 1.01f, animDuration, Interpolation.smooth)
        val scaleAnim4 = scaleTo(1f, 1f, animDuration, Interpolation.smooth)
        addAction(repeat(100, sequence(scaleAnim1, scaleAnim2, delay(animDuration),
                scaleAnim3, scaleAnim4, delay(5f))))
    }

    override fun act(delta: Float) {
        super.act(delta)

        centerX = x + (width / 2)
        centerY = y + (height / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(region, x, y, width, height)
        drawPlayIcon(batch)
    }

    private fun drawPlayIcon(batch: Batch){
        val iconWidth = playIcon.originalWidth.toFloat()
        val iconHeight = playIcon.originalHeight.toFloat()
        val x = centerX - (iconWidth / 2)
        val y = centerY - (iconHeight / 2)

        batch.draw(playIcon, x, y, x, y, iconWidth, iconHeight, scaleX, scaleY, 0f)
    }

    override fun move() {
        addPlayIconAnimationMove()
    }

    private fun addPlayIconAnimationMove(){
        val animDuration = 1f
        val delayAfter = 0.5f
        val moveToOutside = moveTo(Gdx.graphics.width.toFloat(), y, animDuration, Interpolation.exp10)
        val action = run(Runnable {
            ScreenManager.setScreen(GameScreen())
        })
        addAction(sequence(moveToOutside, delay(delayAfter), action))
    }
}