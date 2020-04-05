package com.mygdx.game.actors.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.mygdx.game.api.Animated
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.api.Physical
import java.util.*

class Arm(manager : AssetManager, private val cookie: Cookie) : GameActor(), Physical, Animated {

    private val texture = manager.get(Descriptors.environment)
    private val handRegion = texture.findRegion(Assets.EnvironmentAtlas.HAND)
    private val catchCookieRegion = texture.findRegion(Assets.EnvironmentAtlas.CATCH_COOKIE)
    private val handRegions = texture.findRegions(Assets.EnvironmentAtlas.HAND)
    private val prolongationArmRegion = texture.findRegion(Assets.EnvironmentAtlas.PROLONGATION_HAND)
    private val handAnim = Animation(0.1f, handRegions, Animation.PlayMode.LOOP_PINGPONG)

    private val random = Random()
    private var runTime = 0f
    private val initPosition = Vector2(0f , Gdx.graphics.height/3f)
    private var limitX: Float = 0.0f
    private var limitY: Float = 0.0f
    private var moveToAction = MoveToAction()
    private var currentFrame: TextureRegion = handRegion

    private var isFinalAnimation = false
    private var isFinalHandBackAnimation = false

    init {
        this.x = initPosition.x
        this.y = initPosition.y
        width = handRegion.originalWidth.toFloat()
        height = handRegion.originalHeight.toFloat()
        setMoveAction()
    }

    override fun act(delta: Float) {
        super.act(delta)
        if(actions.isEmpty) setMoveAction()

        runTime += delta
    }

    private fun setMoveAction(){
        moveToAction = MoveToAction()
        limitX = -getLimit()
        limitY = getLimit()
        moveToAction.setPosition(initPosition.x + limitX, initPosition.y + limitY)
        moveToAction.duration = 2f
        moveToAction.interpolation = Interpolation.smooth
        this.addAction(moveToAction)
    }

    private fun getLimit(max : Int = 50): Float{
        return random.nextInt(max).toFloat()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if(isFinalAnimation.not()){
            currentFrame = handAnim.getKeyFrame(runTime)
        }
/*        if(handAnim.isAnimationFinished(runTime)){
            currentFrame = when(random.nextInt(3)){
                2 -> {
                    runTime = 0f
                    handAnim.getKeyFrame(runTime)
                }
                else -> handRegion
            }
        }*/
        val width = currentFrame.regionWidth.toFloat()
        val height =  currentFrame.regionHeight.toFloat()
        drawProlongationHand(batch!!)
        batch.draw(currentFrame, x, y, width, height)
    }

    private fun drawProlongationHand(batch: Batch) {
        val region = prolongationArmRegion
        val width = region.originalWidth.toFloat()
        val height = region.originalHeight.toFloat()
        val y = if(isFinalHandBackAnimation) {
            y+23 // fit to hand
        } else y
        batch.draw(region, x - region.originalWidth, y + 73, width, height)
    }

    override fun getBoundsRect(): Rectangle {
        return Rectangle(x, y, width, height)
    }

    override fun animate(isReverse: Boolean, runAfter: Runnable) {
        isFinalAnimation = true
        val animDuration = 0.5f
       if(isReverse){
            catchCookieAnimation(animDuration, runAfter)
        }else{
           val y = 100f
           val move = Actions.moveTo(x, y, animDuration)
           val run = Actions.run(runAfter)
           val sequence = Actions.sequence(move, run)
           addAction(sequence)
        }
    }

    private fun catchCookieAnimation(animDuration: Float, runAfter: Runnable){
        currentFrame = handRegion
        val move = Actions.moveTo(cookie.x - 120, cookie.y + 40, animDuration)
        val runAfterMove =  Actions.run {
            cookie.remove()
            currentFrame = catchCookieRegion
            isFinalHandBackAnimation = true
        }
        val backAnimation = Actions.moveTo(-currentFrame.regionWidth.toFloat(), y, animDuration)
        val run = Actions.run(runAfter)
        val sequence = Actions.sequence(move, runAfterMove, backAnimation, run)
        addAction(sequence)
    }
}