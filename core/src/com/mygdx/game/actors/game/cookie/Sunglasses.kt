package com.mygdx.game.actors.game.cookie

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.Config
import com.mygdx.game.api.*
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class Sunglasses(manager: AssetManager, val cookie: Cookie) : GameActor(), Animated {

    private val texture = manager.get(Descriptors.cookie)
    private val jumpUpRegion = texture.findRegion(Assets.CookieAtlas.JUMP_UP_SUNGLASSES)
    private val jumpDownRegion = texture.findRegion(Assets.CookieAtlas.JUMP_DOWN_SUNGLASSES)
    private val runRegions = texture.findRegions(Assets.CookieAtlas.RUN_SUNGLASSES)
    private val sunglasses = texture.findRegion(Assets.CookieAtlas.SUNGLASSES)
    private val runAnimation = Animation(0.1f, runRegions, Animation.PlayMode.LOOP_PINGPONG)
    private var currentFrame = runAnimation.keyFrames.first()
    private var isInvolvedInGame = false
    private var isGameOver = false

    init {
        width = sunglasses.originalWidth.toFloat()
        height = sunglasses.originalHeight.toFloat()
        x = (Config.WIDTH_GAME - width) / 2
        y = Config.HEIGHT_GAME - height - 50
        setScale(0.1f)
        color.a = 0f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        currentFrame = when {
            cookie.isRun() -> runAnimation.getKeyFrame(cookie.runTime)
            cookie.isJump() -> jumpUpRegion
            cookie.isFalling() -> jumpDownRegion
            cookie.isVisible.not() -> jumpUpRegion
            else -> null
        }

        if(isInvolvedInGame.not()) {
            batch.setColor(color.r, color.g, color.b, color.a)
            batch.draw(sunglasses, x,  y, 0f, 0f, width, height, scaleX, scaleY, rotation)
        }

        batch.setColor(color.r, color.g, color.b, 1f)
        if(isInvolvedInGame) {
            val x = if(isGameOver) x else cookie.x
            val y = if(isGameOver) y else cookie.y
            batch.draw(currentFrame, x, y, cookie.width, cookie.height)
        }
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val action = when (type) {
            AnimationType.SHOW_ON_SCENE -> {
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
                )
            }
            AnimationType.HIDE_FROM_SCENE -> {
                if(isInvolvedInGame) {
                    Actions.sequence(
                            Actions.run {
                                isGameOver = true
                                y = cookie.y + (cookie.width / 2)
                                x = cookie.x + (cookie.height / 2)
                            },
                            Actions.moveTo(x, -200f, 0.3f, Interpolation.exp10)
                    )
                } else null
            }
            else -> null
        }
        val run = Actions.run(runAfter)
        val sequence = Actions.sequence(action, run)
        addAction(sequence)
    }
}