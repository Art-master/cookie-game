package com.mygdx.game.actors.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.Config
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.impl.Physical
import com.mygdx.game.impl.Scrollable

class Cookie(manager : AssetManager, private val startY: Float,
        startX: Float = Config.widthGame/2): Actor(), Scrollable, Physical{

    val position = Vector2(startX, startY)
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
    private var startJumpY = 0f
    private var ground = startY

    init {
        updateCoordinates()
        width = texture.findRegion(Assets.CookieAtlas.RUN).originalWidth.toFloat()
        height = texture.findRegion(Assets.CookieAtlas.RUN).originalHeight.toFloat()
    }

    private fun updateCoordinates(){
        x = position.x
        y = position.y
    }

    override fun act(delta: Float) {
        super.act(delta)
        runTime += delta
        updateGravity()
        updateActorState()
        position.add(velocity.cpy().scl(delta))
        updateCoordinates()
        rectangle.set(position.x, position.y, width, height)
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
        batch.draw(currentFrame, x, y, width, height)
    }

    fun isFalling() = state == State.FALL
    fun isRun() = state == State.RUN
    fun isJump() = state == State.JUMP

    fun startJumpForce() {
        if(state == State.RUN){
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
            }else{
                 againstThe(obj)
            }
        }
        if(isAfter(obj) && state == State.RUN){
            state = State.FALL
        }
    }

    private fun isAfter(obj : RandomTableItem) : Boolean{
        val tailObj = obj.x + obj.width
        return x >= tailObj && x < tailObj + 30f
    }

    private fun getTailX() = x + width
    private fun isHigherThen(obj : RandomTableItem) = y > obj.y + obj.height - 30
    private fun setOnTop(obj : RandomTableItem) {
        resetState()
        position.y = obj.y + obj.height
    }
    private fun againstThe(obj : RandomTableItem) {position.x = obj.x - width}

    override fun getBoundsRect() = rectangle
}