package com.mygdx.game.actors.main_menu_screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.mygdx.game.Config
import com.mygdx.game.api.Animated
import com.mygdx.game.api.AnimationType
import com.mygdx.game.api.GameActor

class PlayButton(manager: AssetManager) : GameActor(), Animated {

    private val texture = manager.get(Descriptors.menu)
    private var region = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON)
    private var playIcon = texture.findRegion(Assets.MainMenuAtlas.BUTTON_PLAY)

    private var centerX = 0f
    private var centerY = 0f

    init {
        isVibrating = true
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
        addAction(repeat(RepeatAction.FOREVER, sequence(scaleAnim1, scaleAnim2, delay(animDuration),
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

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = Config.BUTTONS_ANIMATION_TIME_S
        val sequence: SequenceAction = when(type){
            AnimationType.HIDE_FROM_SCENE -> {
                val runBefore = run(Runnable {
                    region = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON_CRASH)
                })
                sequence(runBefore)
            }
            AnimationType.SHOW_ON_SCENE -> {
                val animation = scaleTo(1f, 1f, animDuration / 2)
                sequence(animation, run(runAfter))
            }
            else -> return
        }
        addAction(sequence)
    }
}