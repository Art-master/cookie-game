/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.game.cookie

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Array
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.Config.ItemScrollSpeed
import com.run.cookie.run.game.DebugUtils
import com.run.cookie.run.game.actors.game.TableItem
import com.run.cookie.run.game.actors.game.TableItem.Structure
import com.run.cookie.run.game.api.*
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import com.run.cookie.run.game.managers.VibrationManager
import com.run.cookie.run.game.managers.VibrationManager.VibrationType.*

class Cookie(private val manager: AssetManager,
             val startY: Float,
             var startX: Float) : GameActor(), Scrollable, Physical, Animated {

    private val position = Vector2(startX, startY)
    private val velocity = Vector2(0f, 0f)
    private var velocityJump = Config.VELOCITY_JUMP
    private var maxJumpHeight = Config.MAX_JUMP_HEIGHT
    private var gravity = Config.GRAVITY

    private val texture = manager.get(Descriptors.cookie)
    private val jumpUpAnimation = texture.findRegion(Assets.CookieAtlas.JUMP_UP)
    private val jumpDownAnimation = texture.findRegion(Assets.CookieAtlas.JUMP_DOWN)
    private val winnerRegion = texture.findRegion(Assets.CookieAtlas.WINNER)
    private val runRegions = texture.findRegions(Assets.CookieAtlas.RUN)
    private val runAnimation = Animation(0.1f, runRegions, Animation.PlayMode.LOOP_PINGPONG)

    private var currentFrame = runAnimation.getKeyFrame(0f)

    var runTime = 0f
        private set

    private val rectangle: Rectangle = Rectangle()

    enum class State { INIT, RUN, JUMP, FALL, STUMBLE, SLIP, STOP, WIN }

    var state = State.INIT
        private set

    private var startJumpY = 0f
    private var jumpPeakValue = 0f

    private var isStopUpdateX = false
    private var isStopUpdateY = false

    private var ground = startY

    private var move = HorizontalScroll(startX, startY, currentFrame.originalWidth, currentFrame.originalHeight)

    val listeners: Array<CookieLifecycle> = Array()

    init {
        width = runRegions[0].originalWidth.toFloat()
        height = runRegions[0].originalHeight.toFloat()
        x = -width
        y = startY
    }

    private fun updateCoordinates() {
        if (isStopUpdateX.not()) x = move.getX()
        if (isStopUpdateY.not()) y = position.y
    }

    override fun act(delta: Float) {
        super.act(delta)
        runTime += delta
        if (state == State.STOP) return
        if (state != State.INIT && state != State.WIN) {
            updateGravity()
            updateActorState()
            position.add(velocity.x * delta, velocity.y * delta)
            updateCoordinates()
            move.act(delta)
            controlCookieVelocity()
        } else if (state == State.WIN) {
            move.act(delta)
            updateCoordinates()
            if (x >= startX) {
                width = winnerRegion.originalWidth.toFloat()
                height = winnerRegion.originalHeight.toFloat()
            }
            controlCookieVelocity()
        }
        if (y < startY) y = startY // prevent falling
    }

    private fun updateGravity() {
        if (state == State.RUN || state == State.FALL) {
            velocity.add(0f, gravity * runTime)
        }
    }

    private fun updateActorState() {
        when (state) {
            State.FALL -> {
                if (isJumpPeakPassed()) startJumpY = 0f
                else jumpPeakValue = y

                if (isGround()) {
                    listeners.forEach { it.jumpEnd(y <= startY) }
                    resetState()
                }
            }
            State.JUMP -> {
                if (isMaxJump()) state = State.FALL
                else velocity.add(0f, velocityJump)
            }
            State.RUN -> velocity.y = 0f
            else -> return
        }
    }

    fun isJumpPeakPassed() = jumpPeakValue > y

    private fun resetState() {
        state = State.RUN
        runTime = 0f
        velocity.y = 0f
        position.y = startY
        jumpPeakValue = 0f
        maxJumpHeight = Config.MAX_JUMP_HEIGHT
        velocityJump = Config.VELOCITY_JUMP
        gravity = Config.GRAVITY
        normalMove()
        VibrationManager.cancel()
    }

    private fun isGround() = position.y <= startY
    private fun isMaxJump() = position.y >= startJumpY + maxJumpHeight

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        currentFrame = when {
            state === State.JUMP -> jumpUpAnimation
            state === State.FALL && isJumpPeakPassed().not() -> jumpUpAnimation
            state === State.FALL -> jumpDownAnimation
            state === State.WIN && x >= startX -> winnerRegion
            state == State.STOP -> runRegions.first()
            state == State.SLIP -> jumpDownAnimation
            state == State.STUMBLE -> jumpDownAnimation
            else -> runAnimation.getKeyFrame(runTime)
        }
        DebugUtils.drawVerticalLine(batch, manager, startX)

        batch.draw(currentFrame, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
        debugCollidesIfEnable(batch, manager)
    }

    fun startJumpForce() {
        if (state == State.RUN) {
            initJump()
            fastMove()
        }
    }

    private fun initJump(maxJumpHeight: Int = Config.MAX_JUMP_HEIGHT,
                         velocityJump: Float = Config.VELOCITY_JUMP,
                         gravity: Float = Config.GRAVITY) {
        startJumpY = y
        state = State.JUMP
        runTime = 0f
        this.maxJumpHeight = maxJumpHeight
        this.velocityJump = velocityJump
        this.gravity = gravity
    }

    fun endJumpForce() {
        startJumpY = 0f
    }

    override fun stopMove() {
        move.isStopMove = true
        state = State.STOP
    }

    fun caught() {
        stopMove()
        listeners.forEach { it.caught() }
    }

    override fun runMove() {
        move.isStopMove = false
        state = State.RUN
    }

    override fun updateSpeed() {
        move.update()
    }

    fun checkCollides(obj: TableItem) {
        if (state == State.WIN || state == State.STOP) return
        if (collides(obj)) {
            if (obj.structure == Structure.SHARP) {
                stumble(obj)
                return
            } else if (obj.structure == Structure.ICE) {
                slip(obj)
                return
            }

            if (isStepOnObject(obj) && startJumpY == 0f && jumpPeakValue - y > 5) {
                setOnTop(obj)
            } else if (!isFalling() && aheadOfObj(obj) && getTop(obj) <= startY + 20) {
                setOnTop(obj)
            } else if (isForward(obj) && !isHigherThen(obj)) {
                inFrontOfTheObject(obj)
            } else if (isFalling() && y < obj.getBoundsTop()) setOnTop(obj)
        }

        if (isAfterObject(obj) && state == State.RUN) {
            state = State.FALL
            fastMove()
        }

        if (inBoundaries(obj)) {
            ground = getTop(obj)
        } else if (isAfterObject(obj)) {
            ground = startY
        }
    }

    private fun isFalling() = state == State.FALL && jumpPeakValue > y
    private fun isForward(obj: TableItem) = getBoundsTail() < obj.getBoundsRect().x + 20
    private fun isHigherThen(obj: TableItem) = getBoundsRect().y > getTop(obj) - 20
    private fun aheadOfObj(obj: TableItem) = getBoundsTail() < obj.getBoundsRect().x + 30

    private fun setOnTop(obj: TableItem) {
        obj.jumpedOnAction()
        ground = getTop(obj)
        resetState()
        when (obj.structure) {
            Structure.STICKY -> slowMove()
            Structure.JELLY -> {
                move.update(speed = ItemScrollSpeed.VERY_FAST_MOVE)
                initJump(120, 170f, Config.GRAVITY)
            }
            else -> {
            }
        }
        //obj.animate(AnimationType.ITEM_SQUASH)
        position.y = getTop(obj)
    }

    private fun controlCookieVelocity() {
        if (state == State.STUMBLE || state == State.SLIP) return
        if (move.scrollSpeed != ItemScrollSpeed.NONE && x > startX) {
            if (move.scrollSpeed == ItemScrollSpeed.FAST_MOVE) normalMove()
            x = startX
        }
    }

    private fun normalMove() {
        move.update(x = x, speed = ItemScrollSpeed.NONE)
    }

    private fun fastMove() {
        move.update(x = x, speed = ItemScrollSpeed.FAST_MOVE)
    }

    private fun slowMove() {
        move.update(x = x, speed = ItemScrollSpeed.SLOW_MOVE)
        VibrationManager.vibrate(STICKY_ITEM)
    }

    private fun inFrontOfTheObject(obj: TableItem) {
        when (obj.structure) {
            Structure.JELLY -> {
                velocity.y = velocityJump
                initJump(60, 50f, -200f)
                move.update(speed = ItemScrollSpeed.VERY_FAST_MOVE_BACK)
            }
            else -> setAgainstTheObject(obj)
        }
    }

    /**
     * Execute when cookie stumbled by pushpin or the same thing.
     * It cookie animation, then cookie go up again
     * @param obj - table object
     */
    private fun stumble(obj: TableItem) {
        if (state == State.STUMBLE) return
        state = State.STUMBLE

        val duration = 0.2f
        isStopUpdateX = true
        isStopUpdateY = true
        move.update(speed = ItemScrollSpeed.LEVEL_2)
        val parallel = Actions.parallel(
                Actions.moveTo(x, startY + 100, duration, Interpolation.exp10),
                Actions.rotateTo(-90f, duration, Interpolation.exp10))
        val timeout = Actions.delay(0.5f, Actions.run {
            rotation = 0f
            y = startY
            isStopUpdateY = false
            state = State.RUN
            position.y = startY
            fastMove()
            VibrationManager.cancel()
        })
        val sequence = Actions.sequence(
                parallel,
                Actions.run {
                    VibrationManager.vibrate(ACTOR_FALL)
                    listeners.forEach { it.stumbled() }
                    isStopUpdateX = false
                    move.update(x = x)
                }, timeout)
        addAction(sequence)
    }

    /**
     * Execute when cookie slipped by ice puddle or the same thing.
     * It cookie animation, then cookie go up again
     * @param obj - table object
     */
    private fun slip(obj: TableItem) {
        if (state == State.SLIP) return
        state = State.SLIP

        val duration = 0.2f
        isStopUpdateX = true
        isStopUpdateY = true
        move.update(speed = ItemScrollSpeed.NONE)
        val parallel = Actions.parallel(
                Actions.moveTo(x + 30, startY - 30, duration, Interpolation.exp10),
                Actions.rotateTo(90f, duration, Interpolation.exp10))
        val timeout = Actions.delay(0.5f, Actions.run {
            rotation = 0f
            y = startY
            state = State.RUN
            isStopUpdateY = false
            position.y = y
            x -= width / 2
            move.update(x = x)
            fastMove()
            VibrationManager.cancel()
        })
        val sequence = Actions.sequence(
                parallel,
                Actions.run {
                    VibrationManager.vibrate(STICKY_SLIP)
                    isStopUpdateX = false
                    move.update(x = x)
                }, timeout)
        addAction(sequence)
    }

    private fun setAgainstTheObject(obj: TableItem) {
        move.setX(obj.getBoundsRect().x - getBoundsRect().width - 35)
    }

    override fun getBoundsRect(): Rectangle {
        rectangle.set(x + 30, y, width - 60, height)
        return rectangle
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val anim = when (type) {
            AnimationType.SHOW_ON_SCENE -> {
                val animDuration = 3f
                Actions.sequence(
                        Actions.moveTo(startX, startY, animDuration),
                        Actions.run { state = State.RUN })
            }
            else -> null
        }
        val run = Actions.run(runAfter)
        addAction(Actions.sequence(anim, run))
    }

    fun win() {
        startX = Config.WIDTH_GAME / 1.5f
        move.update(speed = ItemScrollSpeed.VERY_FAST_MOVE)
        state = State.WIN
    }
}