package com.mygdx.game.actors

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.ScreenConfig
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.impl.Physical
import com.mygdx.game.impl.Scrollable
import com.mygdx.game.world.GameWorld

class Cookie(manager : AssetManager,
        startX: Float = ScreenConfig.widthGame/2,
        private val startY: Float = 50f): Actor(), Scrollable, Physical{

    val position = Vector2(startX, startY)
    private val velocity = Vector2(0f, 0f)
    private val velocityJump = 20f

    private val acceleration = Vector2(0f, -10f)

    private val texture = manager.get(Descriptors.cookie)
    private val jumpUpAnimation = texture.findRegion(Assets.CookieAtlas.JUMP_UP)
    private val jumpDownAnimation = texture.findRegion(Assets.CookieAtlas.JUMP_DOWN)
    private val runRegions = texture.findRegions(Assets.CookieAtlas.RUN)
    private val runAnimation = Animation(0.1f, runRegions, Animation.PlayMode.LOOP)

    private val maxJumpHeight = 200

    private var currentFrame = runAnimation.getKeyFrame(0f)

    private var runTime = 0f
    private var isStopAnimation = false

    private val rectangle : Rectangle = Rectangle()

    enum class State{ RUN, JUMP, JUMPING,  FALL, FALLING }

    private val gravity = -5f

    private var state = State.RUN

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
        velocity.add(0f, gravity * runTime)
        when(state){
            State.FALL -> {
                //runTime = 0f
/*                velocity.add(0f, -velocityJump * delta)
                if(isUnderGround()){
                    state = State.RUN
                    runTime = 0f
                    velocity.y = 0f
                    position.y = startY
                }*/
            }
            State.FALLING -> {

            }
            State.JUMP -> {
                velocity.add(0f, velocityJump)
                if(isCeiling()) state = State.JUMPING
            }
            State.JUMPING -> {
                if(isCeiling()) state = State.FALL
            }
            State.RUN -> {velocity.y = 0f}
            }


        position.add(velocity.cpy().scl(delta))
        updateCoordinates()
        rectangle.set(position.x, position.y, width, height)
    }

    private fun isGround() = position.y == startY
    private fun isUnderGround() = position.y < startY
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

    fun onClick() {
        if(state != State.JUMP) state = State.JUMP
    }

    override fun stopMove() {
        isStopAnimation = true
    }

    override fun runMove() {
        isStopAnimation = false
    }

    fun resetY(){
        //if(isJump().not()) y = startY
    }

    override fun getBoundsRect() = rectangle
}