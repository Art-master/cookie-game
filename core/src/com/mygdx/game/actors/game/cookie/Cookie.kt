package com.mygdx.game.actors.game.cookie

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.Config
import com.mygdx.game.Config.ItemScrollSpeed
import com.mygdx.game.actors.game.RandomTableItem
import com.mygdx.game.actors.game.RandomTableItem.Structure
import com.mygdx.game.api.*
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.managers.AudioManager

class Cookie(private val manager: AssetManager,
             val startY: Float,
             var startX: Float) : GameActor(), Scrollable, Physical, Animated {

    private val position = Vector2(startX, startY)
    private val velocity = Vector2(0f, 0f)
    private val velocityJump = 50f
    private val maxJumpHeight = 200
    private val gravity = -50f

    private val texture = manager.get(Descriptors.cookie)
    private val jumpUpAnimation = texture.findRegion(Assets.CookieAtlas.JUMP_UP)
    private val jumpDownAnimation = texture.findRegion(Assets.CookieAtlas.JUMP_DOWN)
    private val winnerRegion = texture.findRegion(Assets.CookieAtlas.WINNER)
    private val runRegions = texture.findRegions(Assets.CookieAtlas.RUN)
    private val runAnimation = Animation(0.1f, runRegions, Animation.PlayMode.LOOP_PINGPONG)

    private var currentFrame = runAnimation.getKeyFrame(0f)

    var runTime = 0f
        private set

    private var isStopAnimation = false

    private val rectangle: Rectangle = Rectangle()

    enum class State { RUN, JUMP, FALL, STUMBLE, SLIP }

    var state = State.RUN

    private var jumpFlag = false
    var isHide = false
    private var startJumpY = 0f

    private var isStopUpdateX = false
    private var isStopUpdateY = false

    var ground = startY
        private set

    private var isStartingAnimation = true
    var isWinningAnimation = false
        set(value) {
            startX = Config.WIDTH_GAME / 1.5f
            move.update(speed = ItemScrollSpeed.VERY_FAST_MOVE)
            field = value
        }

    private var move = HorizontalScroll(startX, startY, currentFrame.originalWidth, currentFrame.originalHeight)

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
        if (isStartingAnimation.not() && isWinningAnimation.not()) {
            updateGravity()
            updateActorState()
            position.add(velocity.cpy().scl(delta))
            updateCoordinates()
            move.update(delta)
            controlCookieVelocity()
        } else if (isWinningAnimation) {
            move.update(delta)
            updateCoordinates()
            if (x >= startX) {
                width = winnerRegion.originalWidth.toFloat()
                height = winnerRegion.originalHeight.toFloat()
            }
            controlCookieVelocity()
        }
    }

    private fun updateGravity() {
        if (state == State.STUMBLE || state == State.SLIP) return
        velocity.add(0f, gravity * runTime)
    }

    private fun updateActorState() {
        when (state) {
            State.FALL -> {
                if (isGround()) resetState()
            }
            State.JUMP -> {
                if (jumpFlag && isMaxJump().not()) {
                    velocity.add(0f, velocityJump)
                } else state = State.FALL
            }
            State.RUN -> velocity.y = 0f
            else -> return
        }
    }

    private fun resetState() {
        state = State.RUN
        runTime = 0f
        velocity.y = 0f
        position.y = startY
    }

    private fun isGround() = position.y <= startY
    private fun isMaxJump() = position.y >= startJumpY + maxJumpHeight
    private fun isCeiling() = position.y >= maxJumpHeight

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        currentFrame = when {
            state === State.JUMP -> jumpUpAnimation
            state === State.FALL -> jumpDownAnimation
            isWinningAnimation && x >= startX -> winnerRegion
            isStopAnimation -> runRegions.first()
            else -> runAnimation.getKeyFrame(runTime)
        }

        if (isHide.not()) {
            batch.draw(currentFrame, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
        }
        debugCollidesIfEnable(batch, manager)
    }

    fun startJumpForce() {
        if (isWinningAnimation || isStartingAnimation) return
        if (state == State.RUN) {
            AudioManager.play(AudioManager.SoundApp.JUMP)
            initJump()
            fastMove()
        }
    }

    private fun initJump() {
        startJumpY = y
        state = State.JUMP
        runTime = 0f
        jumpFlag = true
    }

    fun endJumpForce() {
        jumpFlag = false
        startJumpY = 0f
    }

    override fun stopMove() {
        isStopAnimation = true
        move.isStopMove = true
    }

    override fun runMove() {
        move.isStopMove = false
        isStopAnimation = false
    }

    override fun updateSpeed() {
        move.update()
    }

    fun checkCollides(obj: RandomTableItem) {
        if (isWinningAnimation) return
        if (collides(obj)) {
            if (obj.structure == Structure.SHARP) {
                stumble(obj)
                return
            }
            if (obj.structure == Structure.ICE) {
                slip(obj)
                return
            }

            if (isHigherThen(obj)) {
                setOnTop(obj)
                obj.jumpedOn()
            } else if (isForward(obj) && getTop(obj) - y < 20) {
                setOnTop(obj)
            } else inFrontOfTheObject(obj)
        }
        if (isAfterObject(obj) && state == State.RUN) {
            state = State.FALL
            fastMove()
        }

        if (isAboveObject(obj)) {
            ground = getTop(obj)
        } else if (isAfterObject(obj)) {
            ground = startY
        }
    }

    private fun isForward(obj: RandomTableItem) = x < obj.getBoundsRect().x + 10
    private fun isHigherThen(obj: RandomTableItem) = y > getTop(obj) - 30

    private fun setOnTop(obj: RandomTableItem) {
        resetState()
        when (obj.structure) {
            Structure.STICKY -> slowMove()
            Structure.JELLY -> initJump()
            else -> {
            }
        }
        obj.animate(AnimationType.ITEM_SQUASH)
        position.y = getTop(obj)
    }

    private fun controlCookieVelocity() {
        if (state == State.STUMBLE || state == State.SLIP) return
        if (move.scrollSpeed != ItemScrollSpeed.NONE && x > startX) {
            x = startX
            if (move.scrollSpeed == ItemScrollSpeed.FAST_MOVE) normalMove()
        }
    }

    private fun normalMove() {
        move.update(speed = ItemScrollSpeed.NONE)
    }

    private fun fastMove() {
        move.update(speed = ItemScrollSpeed.FAST_MOVE)
    }

    private fun slowMove() {
        move.update(speed = ItemScrollSpeed.SLOW_MOVE)
    }

    private fun inFrontOfTheObject(obj: RandomTableItem) {
        when (obj.structure) {
            else -> setAgainstTheObject(obj)
        }
    }

    /**
     * Execute when cookie stumbled by pushpin or the same thing.
     * It run animation, then cookie go up again
     * @param obj - table object
     */
    private fun stumble(obj: RandomTableItem) {
        if (state == State.STUMBLE) return
        state = State.STUMBLE

        isStopAnimation = true
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
            isStopAnimation = false
            position.y = startY
            fastMove()
        })
        val sequence = Actions.sequence(
                parallel,
                Actions.run {
                    isStopUpdateX = false
                    move.update(x = x)
                }, timeout)
        addAction(sequence)
    }

    /**
     * Execute when cookie slipped by ice puddle or the same thing.
     * It run animation, then cookie go up again
     * @param obj - table object
     */
    private fun slip(obj: RandomTableItem) {
        if (state == State.SLIP) return
        state = State.SLIP

        isStopAnimation = true
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
            isStopAnimation = false
            position.y = y
            move.update(x = x - width / 2)
            fastMove()
        })
        val sequence = Actions.sequence(
                parallel,
                Actions.run {
                    isStopUpdateX = false
                    move.update(x = x)
                }, timeout)
        addAction(sequence)
    }

    private fun setAgainstTheObject(obj: RandomTableItem) {
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
                        Actions.run { isStartingAnimation = false })
            }
            else -> null
        }
        val run = Actions.run(runAfter)
        addAction(Actions.sequence(anim, run))
    }
}