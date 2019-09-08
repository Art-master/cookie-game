package com.mygdx.game.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.ScreenConfig
import com.mygdx.game.data.AssetLoader
import com.mygdx.game.impl.Phisical
import com.mygdx.game.impl.Scrollable

class Cookie(
        startX: Float = ScreenConfig.widthGame/2,
        private val startY: Float = 50f,
        private val widthCookie: Float  = AssetLoader.run1.originalWidth.toFloat(),
        private val heightCookie: Float =  AssetLoader.run1.originalHeight.toFloat()) : Actor(), Scrollable{

    private val position = Vector2(startX, startY)
    private val velocity = Vector2(0f, 0f)
    private val acceleration = Vector2(0f, -1f)

    private val maxJumpHeight = position.y + heightCookie * 2
    private var jumpFlag = false

    private val jumpUpAnimation = AssetLoader.jumpUp
    private val jumpDownAnimation = AssetLoader.jumpDown
    private val runAnimation = AssetLoader.runAnimation

    private var currentFrame = runAnimation.getKeyFrame(0f)

    private var runTime = 0f
    private var isStopAnimation = false

    private val rectangle : Rectangle = Rectangle()

    init {
        x = position.x
        y = position.y
        width = widthCookie
        height = heightCookie
    }

    override fun act(delta: Float) {
        super.act(delta)
        runTime += delta
        if (position.y >= maxJumpHeight) jumpFlag = true
        if(position.y < startY){
            jumpFlag = false
            velocity.y = 0f
            position.y = startY
        }

        if(jumpFlag){
            velocity.y = -800f
            velocity.add(acceleration.cpy().scl(delta))
        }

        position.add(velocity.cpy().scl(delta))
        rectangle.set(position.x, position.y, widthCookie, heightCookie)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        currentFrame = when {
            isJump() -> jumpUpAnimation
            isFalling() -> jumpDownAnimation
            else -> runAnimation.getKeyFrame(runTime)
        }
        if(isStopAnimation) currentFrame = AssetLoader.run1
        batch.draw(currentFrame, position.x, position.y, widthCookie, heightCookie)
    }

    fun isFalling() = jumpFlag
    fun isRun() = velocity.y == startY
    fun isJump() = velocity.y > 0

    fun onClick() {
        if(jumpFlag.not()) velocity.y = 800f
    }

    override fun stopMove() {
        isStopAnimation = true
    }

    override fun runMove() {
        isStopAnimation = false
    }

    fun collides(actor : Phisical): Boolean {

        return false
    }
}