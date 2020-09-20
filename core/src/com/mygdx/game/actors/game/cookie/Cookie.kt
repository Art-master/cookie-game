package com.mygdx.game.actors.game.cookie

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.Config
import com.mygdx.game.actors.game.RandomTableItem
import com.mygdx.game.actors.game.RandomTableItem.Structure
import com.mygdx.game.api.*
import com.mygdx.game.Config.ItemScrollSpeed
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.managers.AudioManager

class Cookie(private val manager : AssetManager,
             val startY: Float,
             var startX: Float): GameActor(), Scrollable, Physical, Animated{

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

    private val rectangle : Rectangle = Rectangle()

    enum class State{ RUN, JUMP, FALL}

    var state = State.RUN

    private var jumpFlag = false
    var isHide = false
    private var startJumpY = 0f

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

    private fun updateCoordinates(){
        x = move.getX()
        y = position.y
    }

    override fun act(delta: Float) {
        super.act(delta)
        runTime += delta
        if(isStartingAnimation.not() && isWinningAnimation.not()){
            updateGravity()
            updateActorState()
            position.add(velocity.cpy().scl(delta))
            updateCoordinates()
            move.update(delta)
            controlCookieVelocity()
        } else if(isWinningAnimation){
            move.update(delta)
            updateCoordinates()
            if(x >= startX){
                width = winnerRegion.originalWidth.toFloat()
                height = winnerRegion.originalHeight.toFloat()
            }
            controlCookieVelocity()
        }
    }

    private fun updateGravity(){
        velocity.add(0f, gravity * runTime)
    }

    private fun updateActorState(){
        when(state){
            State.FALL -> {
                if(isGround()) resetState()
            }
            State.JUMP -> {
                if(jumpFlag && isMaxJump().not()){
                    velocity.add(0f, velocityJump)
                } else state = State.FALL
            }
            State.RUN -> velocity.y = 0f
        }
    }

    private fun resetState(){
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
            isJump() -> jumpUpAnimation
            isFalling() -> jumpDownAnimation
            isWinningAnimation && x >= startX -> winnerRegion
            isStopAnimation -> runRegions.first()
            else -> runAnimation.getKeyFrame(runTime)
        }

        if(isHide.not()) {
            batch.draw(currentFrame, x, y, width, height)
        }
        debugCollidesIfEnable(batch, manager)
    }

    fun isFalling() = state == State.FALL
    fun isRun() = state == State.RUN
    fun isJump() = state == State.JUMP

    fun startJumpForce() {
        if(isWinningAnimation || isStartingAnimation) return
        if(state == State.RUN){
            AudioManager.play(AudioManager.SoundApp.JUMP)
            initJump()
            fastMove()
        }
    }

    private fun initJump(){
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

    fun checkCollides(obj : RandomTableItem){
        if(isWinningAnimation) return
        if(collides(obj)){
             if(isHigherThen(obj)){
                 setOnTop(obj)
                 obj.jumpedOn()
            }else if(isForward(obj) && getTop(obj) - y < 20){
                 setOnTop(obj)
            }else setAgainstTheObject(obj)
        }
        if(isAfterObject(obj) && state == State.RUN){
            state = State.FALL
            fastMove()
        }

        if(isAboveObject(obj)){
            ground = getTop(obj)
        } else if(isAfterObject(obj)){
            ground = startY
        }
    }

    private fun isForward(obj : RandomTableItem) = x < obj.getBoundsRect().x + 10
    private fun isHigherThen(obj : RandomTableItem) = y > getTop(obj) - 30

    private fun setOnTop(obj : RandomTableItem) {
        resetState()
        when(obj.structure) {
            Structure.STICKY -> slowMove()
            Structure.JELLY -> initJump()
            else -> {}
        }
        obj.animate(AnimationType.ITEM_SQUASH)
        position.y = getTop(obj)
    }

    private fun controlCookieVelocity(){
        if(move.scrollSpeed != ItemScrollSpeed.NONE && x > startX) {
            x = startX
            if(move.scrollSpeed == ItemScrollSpeed.FAST_MOVE) normalMove()
        }
    }

    private fun normalMove(){
        move.update(speed = ItemScrollSpeed.NONE)
    }

    private fun fastMove(){
        move.update(speed = ItemScrollSpeed.FAST_MOVE)
    }

    private fun slowMove(){
        move.update(speed = ItemScrollSpeed.SLOW_MOVE)
    }

    private fun setAgainstTheObject(obj : RandomTableItem) {
        move.setX(obj.getBoundsRect().x - getBoundsRect().width - 35)
    }

    override fun getBoundsRect(): Rectangle{
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