package com.mygdx.game.actors.game.cookie

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Array
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
    private val velocityJump = Config.VELOCITY_JUMP
    private val maxJumpHeight = Config.MAX_JUMP_HEIGHT
    private val gravity = Config.GRAVITY

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
            position.add(velocity.cpy().scl(delta))
            updateCoordinates()
            move.update(delta)
            controlCookieVelocity()
        } else if (state == State.WIN) {
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
        if (state == State.RUN || state == State.JUMP || state == State.FALL) {
            velocity.add(0f, gravity * runTime)
        }
    }

    private fun updateActorState() {
        when (state) {
            State.FALL -> {
                if (isJumpPeakPassed().not()) jumpPeakValue = y
                if (isGround()) {
                    listeners.forEach { it.jumpEnd(y <= startY) }
                    resetState()
                }
            }
            State.JUMP -> {
                if (isMaxJump().not()) {
                    velocity.add(0f, velocityJump)
                } else state = State.FALL
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

        batch.draw(currentFrame, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
        debugCollidesIfEnable(batch, manager)
    }

    fun startJumpForce() {
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

    fun checkCollides(obj: RandomTableItem) {
        if (state == State.WIN || state == State.STOP) return
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
        //obj.animate(AnimationType.ITEM_SQUASH)
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
        })
        val sequence = Actions.sequence(
                parallel,
                Actions.run {
                    listeners.forEach { it.stumbled() }
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