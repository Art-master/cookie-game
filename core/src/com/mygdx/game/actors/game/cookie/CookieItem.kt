package com.mygdx.game.actors.game.cookie

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import com.mygdx.game.Config
import com.mygdx.game.api.*
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class CookieItem(manager: AssetManager, val cookie: Cookie, itemName: String) : GameActor(), Animated {

    private val texture = manager.get(Descriptors.cookie)
    private var jumpUpRegion = texture.findRegion("jump_up_$itemName")
    private var jumpDownRegion = texture.findRegion("jump_down_$itemName")
    private var runRegions = texture.findRegions("run_$itemName")
    private var itemRegion = texture.findRegion(itemName)
    private var circleLoadingRegion = texture.findRegion(Assets.CookieAtlas.LOADING_CIRCLE)
    private val runAnimation = Animation(0.1f, runRegions, Animation.PlayMode.LOOP_PINGPONG)

    private var currentFrame = if (runAnimation.keyFrames.isNotEmpty()) runAnimation.keyFrames.first() else null
    private val frameHeight = currentFrame?.originalHeight?.toFloat()
    private val frameWidth = currentFrame?.originalWidth?.toFloat()

    private var isInvolvedInGame = false
    private var isGameOver = false

    init {
        width = itemRegion.originalWidth.toFloat()
        height = itemRegion.originalHeight.toFloat()
        x = (Config.WIDTH_GAME - width) / 2
        y = Config.HEIGHT_GAME - height - 100
        scaleX = 0.1f
        scaleY = 0.1f
        color.a = 0f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        currentFrame = when {
            currentFrame == null -> null
            cookie.isRun() -> runAnimation.getKeyFrame(cookie.runTime)
            cookie.isJump() -> jumpUpRegion
            cookie.isFalling() -> jumpDownRegion
            cookie.isVisible.not() -> jumpUpRegion
            else -> null
        }

        if (isInvolvedInGame.not()) {
            batch.setColor(color.r, color.g, color.b, color.a)
            batch.draw(itemRegion, x, y, 0f, 0f, width, height, scaleX, scaleY, 0f)
            drawLoadingCircle(batch)
        }

        batch.setColor(color.r, color.g, color.b, 1f)
        if (isInvolvedInGame && currentFrame !== null) {
            val x = if (isGameOver) x else cookie.x
            val y = if (isGameOver) y else cookie.y
            batch.draw(currentFrame, x, y, frameWidth!!, frameHeight!!)
        }
    }

    private fun drawLoadingCircle(batch: Batch){
        val alpha = if(color.a < 0.5f) color.a else 0.5f
        batch.setColor(color.r, color.g, color.b, alpha)
        val width = circleLoadingRegion.originalWidth.toFloat()
        val height = circleLoadingRegion.originalHeight.toFloat()
        batch.draw(circleLoadingRegion,
                x - (width - this.width) / 2,
                y - (height - this.height) / 2, width / 2,
                height / 2, width, height, scaleX, scaleY, rotation)
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val action = when (type) {
            AnimationType.SHOW_ON_SCENE -> {
                Actions.parallel(
                        Actions.sequence(
                                Actions.parallel(
                                        Actions.scaleTo(1f, 1f, 0.6f, Interpolation.smooth),
                                        Actions.alpha(1f, 0.6f)
                                ),
                                Actions.delay(0.6f),
                                Actions.parallel(
                                        Actions.scaleTo(0f, 0f, 0.3f),
                                        Actions.moveTo(x, 0f, 0.6f),
                                        Actions.alpha(0f, 0.3f)
                                ),
                                Actions.run { isInvolvedInGame = true }
                        ),
                        Actions.repeat(10, Actions.sequence(
                                Actions.rotateBy(-360f, 5f),
                                Actions.rotateTo(0f)))
                )

            }
            AnimationType.HIDE_FROM_SCENE -> {
                if (isInvolvedInGame.not()) return
                Actions.sequence(
                        Actions.run {
                            isGameOver = true
                            y = cookie.y + (cookie.width / 2)
                            x = cookie.x + (cookie.height / 2)
                        },
                        Actions.moveTo(x, -200f, 0.3f, Interpolation.exp10)
                )

            }
            else -> null
        }
        val run = Actions.run(runAfter)
        val sequence = Actions.sequence(action, run)
        addAction(sequence)
    }
}