package com.mygdx.game.actors

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.ScreenConfig
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.impl.Physical
import com.mygdx.game.impl.Scrollable

class Cookie(manager : AssetManager,
        startX: Float = ScreenConfig.widthGame/2,
        private val startY: Float = 50f): Actor(), Scrollable{

    private val position = Vector2(startX, startY)
    private val velocity = Vector2(0f, 0f)
    private val acceleration = Vector2(0f, -1f)

    private var jumpFlag = false

    private val texture = manager.get(Descriptors.cookie)
    private val jumpUpAnimation = texture.findRegion(Assets.CookieAtlas.JUMP_UP)
    private val jumpDownAnimation = texture.findRegion(Assets.CookieAtlas.JUMP_DOWN)
    private val runRegions = texture.findRegions(Assets.CookieAtlas.RUN)
    private val runAnimation = Animation(0.1f, runRegions, Animation.PlayMode.LOOP)

    private val maxJumpHeight = position.y + 200 * 2

    private var currentFrame = runAnimation.getKeyFrame(0f)

    private var runTime = 0f
    private var isStopAnimation = false

    private val rectangle : Rectangle = Rectangle()

    init {
        x = position.x
        y = position.y
        width = texture.findRegion(Assets.CookieAtlas.RUN).originalWidth.toFloat()
        height = texture.findRegion(Assets.CookieAtlas.RUN).originalHeight.toFloat()
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
        rectangle.set(position.x, position.y, width, height)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        currentFrame = when {
            isJump() -> jumpUpAnimation
            isFalling() -> jumpDownAnimation
            else -> runAnimation.getKeyFrame(runTime)
        }
        if(isStopAnimation) currentFrame = runRegions[0]
        batch.draw(currentFrame, position.x, position.y, width, height)
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

    fun collides(actor : Physical): Boolean {

        return false
    }
}