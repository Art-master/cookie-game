package com.mygdx.game.actors.comics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.data.Descriptors
import com.mygdx.game.api.Animated
import com.mygdx.game.api.AnimationType
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets

abstract class ComicsFrame(manager: AssetManager) : GameActor(), Animated {
    protected val atlas = manager.get(Descriptors.comics)!!
    protected var region = atlas.findRegion(Assets.ComicsAtlas.FRAME_1)!!
    private var shadow =  manager.get(Descriptors.environment).findRegion(Assets.EnvironmentAtlas.BLACK)
    protected val finalScale = 0.62f
    protected val framePadding = 30f

    init {
        atlas.textures.forEach{ it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)}
        val screenWidth = Gdx.graphics.width.toFloat()
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()
        x = screenWidth
        y = (Gdx.graphics.height - height) / 2f
        scaleX = 0.2F
        scaleY = 0.2F
        color.a = 0f
        touchable = Touchable.disabled
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.setColor(color.r, color.g, color.b, color.a)
        batch.draw(shadow, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.setColor(color.r, color.g, color.b, 1f)
        batch.color = Color.WHITE
        batch.draw(region, x,  y, 0f, 0f, width, height, scaleX, scaleY, rotation)
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = 0.5f
        val action = when (type) {
            AnimationType.SHOW_ON_SCENE -> {
                val x = (Gdx.graphics.width - width) / 2f
               Actions.sequence(
                       Actions.parallel(
                               Actions.scaleTo(1f, 1f, 1f, Interpolation.smooth),
                               Actions.moveTo(x, y, 0.3f, Interpolation.smooth),
                               Actions.repeat(3, Actions.rotateTo(360f, 0.5f)),
                               Actions.alpha(0.8f, 1f)
                       ),
                        Actions.delay(1f),
                        Actions.parallel(
                                Actions.scaleTo(finalScale, finalScale, animDuration),
                                Actions.moveTo(getFinalMoveX(), getFinalMoveY(), animDuration),
                                Actions.alpha(0f, animDuration)
                        )
               )
            }
            else -> return
        }
        val run = Actions.run(runAfter)
        val sequence = Actions.sequence(action, run)
        addAction(sequence)
    }

    abstract fun getFinalMoveX(): Float
    abstract fun getFinalMoveY(): Float
}