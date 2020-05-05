package com.mygdx.game.actors.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.api.*
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.managers.AudioManager

class Cookie(manager : AssetManager,
             private val startY: Float,
             private val startX: Float): GameActor(), Scrollable, Physical, Animated{

    private val position = Vector2(startX, startY)
    private val velocity = Vector2(0f, 0f)
    private val velocityJump = 50f

    private val acceleration = Vector2(0f, -10f)

    private val texture = manager.get(Descriptors.cookie)
    private val jumpUpAnimation = texture.findRegion(Assets.CookieAtlas.JUMP_UP)
    private val jumpDownAnimation = texture.findRegion(Assets.CookieAtlas.JUMP_DOWN)
    private val runRegions = texture.findRegions(Assets.CookieAtlas.RUN)
    private val runAnimation = Animation(0.1f, runRegions, Animation.PlayMode.LOOP_PINGPONG)

    private val maxJumpHeight = 200

    private var currentFrame = runAnimation.getKeyFrame(0f)

    private var runTime = 0f

    private var isStopAnimation = false

    private val rectangle : Rectangle = Rectangle()

    enum class State{ RUN, JUMP, FALL}

    private val gravity = -50f

    var state = State.RUN

    private var jumpFlag = false
    var isHide = false
    private var startJumpY = 0f
    private var ground = startY

    private var isStartingAnimation = true

    init {
        width = runRegions[0].originalWidth.toFloat()
        height = runRegions[0].originalHeight.toFloat()
        x = -width
        y = startY
    }

    private fun updateCoordinates(){
        x = position.x
        y = position.y
    }

    override fun act(delta: Float) {
        super.act(delta)
        runTime += delta
        if(isStartingAnimation.not()){
            updateGravity()
            updateActorState()
            position.add(velocity.cpy().scl(delta))
            updateCoordinates()
        }
        rectangle.set(x, y, width, height)
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
        position.y = ground
    }

    private fun isGround() = position.y <= ground
    private fun isMaxJump() = position.y >= startJumpY + maxJumpHeight
    private fun isCeiling() = position.y >= maxJumpHeight

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        currentFrame = when {
            isJump() -> jumpUpAnimation
            isFalling() -> jumpDownAnimation
            else -> runAnimation.getKeyFrame(runTime)
        }
        if(isStopAnimation) currentFrame = runRegions[0]

        if(isHide.not()) {
            batch.draw(currentFrame, x, y, width, height)
        }
    }

    fun isFalling() = state == State.FALL
    fun isRun() = state == State.RUN
    fun isJump() = state == State.JUMP

    fun startJumpForce() {
        if(state == State.RUN && isStartingAnimation.not()){
            AudioManager.play(AudioManager.Sound.JUMP)
            startJumpY = y
            state = State.JUMP
            runTime = 0f
            jumpFlag = true
        }
    }

    fun endJumpForce() {
        jumpFlag = false
        startJumpY = 0f
    }

    override fun stopMove() {
        isStopAnimation = true
    }

    override fun runMove() {
        isStopAnimation = false
    }

    fun checkCollides(obj : RandomTableItem){
        if(collides(obj)){
             if(isHigherThen(obj)){
                 setOnTop(obj)
                 obj.jumpedOn()
            }else{
                 againstThe(obj)
            }
        }
        if(isAfter(obj) && state == State.RUN){
            state = State.FALL
        }
    }

    private fun isAfter(obj : RandomTableItem) : Boolean{
        val tailObj = obj.getBoundsRect().x + obj.getBoundsRect().width
        return x >= tailObj && x < tailObj + 30f
    }

    private fun getTailX() = x + width
    private fun isHigherThen(obj : RandomTableItem) = y > obj.getBoundsRect().y + obj.getBoundsRect().height - 30
    private fun setOnTop(obj : RandomTableItem) {
        resetState()
        obj.animate(AnimationType.ITEM_SQUASH)
        position.y = obj.getBoundsRect().y + obj.getBoundsRect().height
    }
    private fun againstThe(obj : RandomTableItem) {position.x = obj.getBoundsRect().x - width}

    override fun getBoundsRect() = rectangle

    override fun animate(type: AnimationType, runAfter: Runnable) {
        if(type == AnimationType.SHOW_ON_SCENE){
            val animDuration = 3f
            val move = Actions.moveTo(startX, startY, animDuration)
            val run = Actions.run { isStartingAnimation = false }
            val run2 = Actions.run(runAfter)
            addAction(Actions.sequence(move, run, run2))
        }
    }
}