/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.game

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
import com.badlogic.gdx.utils.Pool
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.actors.game.cookie.Cookie
import com.run.cookie.run.game.api.Animated
import com.run.cookie.run.game.api.AnimationType
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.api.Physical
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import java.util.*
import kotlin.random.Random as RandomK

class Arm(manager: AssetManager, private val cookie: Cookie) : GameActor(), Physical, Animated {

    private val texture = manager.get(Descriptors.environment)
    private val handRegion = texture.findRegion(Assets.EnvironmentAtlas.HAND)
    private val catchCookieRegion = texture.findRegion(Assets.EnvironmentAtlas.CATCH_COOKIE)
    private val handRegions = texture.findRegions(Assets.EnvironmentAtlas.HAND)
    private val prolongationArmRegion = texture.findRegion(Assets.EnvironmentAtlas.PROLONGATION_HAND)
    private val handAnim = Animation(0.1f, handRegions, Animation.PlayMode.LOOP_PINGPONG)

    private val random = Random()
    private var runTime = 0f
    private val initPosition = Vector2(0f, Config.HEIGHT_GAME / 3f)
    private var limitX: Float = 0.0f
    private var limitY: Float = 0.0f
    private var moveToCatchCookieAnimation: MoveToAction? = null
    private var currentFrame: TextureRegion = handRegion

    var isGameOverAnimation = false
    private var isGameOverHandBackAnimation = false
    var isWinningAnimation = false

    private var animationDelay = 0L
    private var stopTimeMs = 0L

    private var moveToActionsPool: Pool<MoveToAction> = object : Pool<MoveToAction>() {
        override fun newObject(): MoveToAction? {
            return MoveToAction()
        }
    }

    private var sequenceActionsPool: Pool<SequenceAction> = object : Pool<SequenceAction>() {
        override fun newObject(): SequenceAction? {
            return SequenceAction()
        }
    }

    init {
        width = handRegion.originalWidth.toFloat()
        height = handRegion.originalHeight.toFloat()
        this.x = -((width + initPosition.x) * 2)
        this.y = initPosition.y
        updateAnimationTimerIfNeed(true)
    }

    private fun updateAnimationTimerIfNeed(isInit: Boolean = false) {
        if (isInit || System.currentTimeMillis() - stopTimeMs >= animationDelay) {
            stopTimeMs = System.currentTimeMillis()
            animationDelay = RandomK.nextLong(1000, 5000)
            handAnim.frameDuration = RandomK.nextDouble(0.05, 0.2).toFloat()
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        updateFinishAnimationIfNeed()
        updateAnimationTimerIfNeed()
        runTime += delta
    }

    private fun updateFinishAnimationIfNeed() {
        if (cookie.state == Cookie.State.STUMBLE) {
            moveToCatchCookieAnimation?.setPosition(cookie.x - 120, cookie.y - 100)
        } else moveToCatchCookieAnimation?.setPosition(cookie.x - 120, cookie.y + 40)
    }

    private fun setMoveAction() {
        val action = moveToActionsPool.obtain()
        action.pool = moveToActionsPool
        limitX = -getLimit()
        limitY = getLimit()
        action.setPosition(initPosition.x + limitX, initPosition.y + limitY)
        action.duration = 2f
        action.interpolation = Interpolation.smooth

        val sequence = sequenceActionsPool.obtain()
        sequence.addAction(action)
        sequence.addAction(Actions.run { setMoveAction() })
        sequence.pool = sequenceActionsPool
        addAction(sequence)
    }

    private fun getLimit(max: Int = 50): Float {
        return random.nextInt(max).toFloat()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = color
        if (isGameOverAnimation.not()) {
            currentFrame = handAnim.getKeyFrame(runTime)
        }
        drawProlongationHand(batch)
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
        val y = if (isGameOverHandBackAnimation) {
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
                val animDuration = 0.5f
                val backAnimation = Actions.moveTo(-currentFrame.regionWidth.toFloat(), y, animDuration)
                val run = Actions.run(runAfter)
                Actions.sequence(backAnimation, run)
            }
            AnimationType.COOKIE_CATCH -> {
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
        val animDuration = 3f
        val move = Actions.moveTo(initPosition.x, initPosition.y, animDuration)
        val run = Actions.run(runAfter)
        return Actions.sequence(move, run)
    }

    private fun catchCookieAnimation(runAfter: Runnable): SequenceAction? {
        val animDuration = 0.2f
        currentFrame = handRegion
        moveToCatchCookieAnimation = Actions.moveTo(0f, 0f, animDuration)
        updateFinishAnimationIfNeed()
        val runAfterMove = Actions.run {
            cookie.isVisible = false
            currentFrame = catchCookieRegion
            isGameOverHandBackAnimation = true
        }
        val run = Actions.run(runAfter)
        return Actions.sequence(moveToCatchCookieAnimation, runAfterMove, run)
    }
}