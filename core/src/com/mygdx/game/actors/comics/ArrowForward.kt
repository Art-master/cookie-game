package com.mygdx.game.actors.comics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import com.mygdx.game.api.Animated
import com.mygdx.game.api.AnimationType
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class ArrowForward(manager: AssetManager) : GameActor(), Animated {
    private val environmentAtlas = manager.get(Descriptors.comics)
    private val region = environmentAtlas.findRegion(Assets.ComicsAtlas.ARROW_RIGHT)

    init {
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()
        x = Gdx.graphics.width - width
        y = 0f
        scaleX = 0.3f
        scaleY = 0.3f
        color.a = 0f
        touchable = Touchable.disabled
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.setColor(color.r, color.g, color.b, color.a)
        batch.draw(region, x, y, 0f, 0f, width, height, scaleX, scaleY, rotation)
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = 0.4f
        val action = when (type) {
            AnimationType.SHOW_ON_SCENE -> {
                Actions.sequence(
                        Actions.parallel(
                                Actions.scaleTo(1f, 1f, animDuration, Interpolation.smooth),
                                Actions.alpha(1f, animDuration)
                        ),
                        getAnimationPulse()
                )
            }
            else -> return
        }
        val run = Actions.run(runAfter)
        val sequence = Actions.sequence(action, run)
        addAction(sequence)
    }

    private fun getAnimationPulse(): RepeatAction {
        val animDuration = 0.05f
        val scaleAnim1 = Actions.scaleTo(1.01f, 1.01f, animDuration, Interpolation.smooth)
        val scaleAnim2 = Actions.scaleTo(1f, 1f, animDuration, Interpolation.smooth)
        val scaleAnim3 = Actions.scaleTo(1.01f, 1.01f, animDuration, Interpolation.smooth)
        val scaleAnim4 = Actions.scaleTo(1f, 1f, animDuration, Interpolation.smooth)
        return Actions.repeat(RepeatAction.FOREVER, Actions.sequence(scaleAnim1, scaleAnim2, Actions.delay(animDuration),
                scaleAnim3, scaleAnim4, Actions.delay(1f)))
    }
}