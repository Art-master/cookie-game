package com.mygdx.game.actors.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.mygdx.game.api.Animated
import com.mygdx.game.api.AnimationType
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.api.Physical
import java.util.*
import kotlin.random.Random as RandomK

class Arm(manager: AssetManager, private val cookie: Cookie) : GameActor(), Physical, Animated {

    private val texture = manager.get(Descriptors.environment)
    private val handRegion = texture.findRegion(Assets.EnvironmentAtlas.HAND)
    private val catchCookieRegion = texture.findRegion(Assets.EnvironmentAtlas.CATCH_COOKIE)
    private val handRegions = texture.findRegions(Assets.EnvironmentAtlas.HAND)
    private val prolongationArmRegion = texture.findRegion(Assets.EnvironmentAtlas.PROLONGATION_HAND)
    private val handAnim = Animation(0.1f, handRegions, Animation.PlayMode.LOOP_PINGPONG)

    private var handAnimTimer = Timer()
    private var isTimerNeedRestart = true

    private val random = Random()
    private var runTime = 0f
    private val initPosition = Vector2(0f, Gdx.graphics.height / 3f)
    private var limitX: Float = 0.0f
    private var limitY: Float = 0.0f
    private var moveToAction = MoveToAction()
    private var moveToCatchCookieAnimation: MoveToAction? = null
    private var currentFrame: TextureRegion = handRegion

    private var isFinalAnimation = false
    private var isFinalHandBackAnimation = false

    init {
        width = handRegion.originalWidth.toFloat()
        height = handRegion.originalHeight.toFloat()
        this.x = -((width + initPosition.x) * 2)
        this.y = initPosition.y
        updateAnimationTimerIfNeed()
    }

    private fun updateAnimationTimerIfNeed() {
        if (isTimerNeedRestart) {
            isTimerNeedRestart = false
            setAnimationHandTimer()
        }
    }

    private fun setAnimationHandTimer() {
        val handAnimationDurationTask = object : TimerTask() {
            override fun run() {
                handAnim.frameDuration = RandomK.nextDouble(0.05, 0.2).toFloat()
                isTimerNeedRestart = true
            }
        }
        val delay = RandomK.nextLong(1000, 5000)
        handAnimTimer.schedule(handAnimationDurationTask, delay)
    }

    override fun act(delta: Float) {
        super.act(delta)
        updateFinishAnimationIfNeed()
        updateAnimationTimerIfNeed()
        runTime += delta
    }

    private fun updateFinishAnimationIfNeed() {
        moveToCatchCookieAnimation?.setPosition(cookie.x - 120, cookie.y + 40)
    }

    private fun setMoveAction() {
        moveToAction = MoveToAction()
        limitX = -getLimit()
        limitY = getLimit()
        moveToAction.setPosition(initPosition.x + limitX, initPosition.y + limitY)
        moveToAction.duration = 2f
        moveToAction.interpolation = Interpolation.smooth
        this.addAction(moveToAction)
    }

    private fun getLimit(max: Int = 50): Float {
        return random.nextInt(max).toFloat()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (isFinalAnimation.not()) {
            currentFrame = handAnim.getKeyFrame(runTime)
        }
        drawProlongationHand(batch!!)
        drawHand(batch)
    }

    private fun drawHand(batch: Batch) {
        val width = currentFrame.regionWidth.toFloat()
        val height = currentFrame.regionHeight.toFloat()
        batch.draw(currentFrame, x, y, width, height)
    }

    private fun drawProlongationHand(batch: Batch) {
        val region = prolongationArmRegion
        val width = region.originalWidth.toFloat()
        val height = region.originalHeight.toFloat()
        val y = if (isFinalHandBackAnimation) {
            y + 23 // fit to hand
        } else y
        batch.draw(region, x - region.originalWidth, y + 73, width, height)
    }

    override fun getBoundsRect(): Rectangle {
        return Rectangle(x, y, width, height)
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val sequence = when (type) {
            AnimationType.HIDE_FROM_SCENE -> {
                isFinalAnimation = true
                catchCookieAnimation(runAfter)
            }
            AnimationType.SHOW_ON_SCENE -> showArmAnimation(runAfter)
            else -> return
        }
        addAction(sequence)
    }

    fun startRepeatableMove() {
        actions.clear()
        setMoveAction()
    }

    private fun showArmAnimation(runAfter: Runnable): SequenceAction? {
        setMoveAction()
        val animDuration = 3f
        val move = Actions.moveTo(initPosition.x, initPosition.y, animDuration)
        val run = Actions.run(runAfter)
        return Actions.sequence(move, run)
    }

    private fun catchCookieAnimation(runAfter: Runnable): SequenceAction? {
        val animDuration = 0.5f
        currentFrame = handRegion
        moveToCatchCookieAnimation = Actions.moveTo(0f, 0f, animDuration)
        updateFinishAnimationIfNeed()
        val runAfterMove = Actions.run {
            cookie.remove()
            currentFrame = catchCookieRegion
            isFinalHandBackAnimation = true
        }
        val backAnimation = Actions.moveTo(-currentFrame.regionWidth.toFloat(), y, animDuration)
        val run = Actions.run(runAfter)
        return Actions.sequence(moveToCatchCookieAnimation, runAfterMove, backAnimation, run)
    }
}