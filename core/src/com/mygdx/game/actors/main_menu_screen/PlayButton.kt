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
import com.mygdx.game.api.Animated

class PlayButton(manager: AssetManager) : Actor(), Animated {

    private val texture = manager.get(Descriptors.menu)
    private var region = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON)
    private var playIcon = texture.findRegion(Assets.MainMenuAtlas.BUTTON_PLAY)

    private var centerX = 0f
    private var centerY = 0f

    init {
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()
        x = (Config.WIDTH_GAME / 2) - width / 2
        y = (Config.HEIGHT_GAME / 6)
        scaleX = 0.1f
        scaleY = 0.1f
        addPlayIconAnimationPulse()
    }

    private fun addPlayIconAnimationPulse() {
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
        batch.draw(region, x, y, x, y, width, height, scaleX, scaleY, 0f)
        drawPlayIcon(batch)
    }

    private fun drawPlayIcon(batch: Batch) {
        val iconWidth = playIcon.originalWidth.toFloat()
        val iconHeight = playIcon.originalHeight.toFloat()
        val x = centerX - (iconWidth / 2)
        val y = centerY - (iconHeight / 2)

        batch.draw(playIcon, x, y, x, y, iconWidth, iconHeight, scaleX, scaleY, 0f)
    }

    override fun animate(isReverse: Boolean, runAfter: Runnable) {
        val animDuration = Config.BUTTONS_ANIMATION_TIME_S
        val move = if (isReverse) {
            moveTo(Gdx.graphics.width.toFloat(), y, animDuration, Interpolation.exp10)
        } else {
            scaleTo(1f, 1f, animDuration / 2)
        }
        val runAfterAnimation = run(runAfter)
        val sequence = if (isReverse) {
            val runBefore = run(Runnable {
                region = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON_2)
            })
            sequence(runBefore)
        } else {
            sequence(move, runAfterAnimation)
        }
        addAction(sequence)
    }
}