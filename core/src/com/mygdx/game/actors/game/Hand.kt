package com.mygdx.game.actors.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import java.util.*

class Hand(manager : AssetManager) : Actor() {
    private val texture = manager.get(Descriptors.environment)
    private val handRegion = texture.findRegion(Assets.EnvironmentAtlas.HAND)
    private val handRegions = texture.findRegions(Assets.EnvironmentAtlas.HAND)
    private val handAnim = Animation(0.1f, handRegions, Animation.PlayMode.LOOP_PINGPONG)

    private val random = Random()
    private var runTime = 0f
    private val initPosition = Vector2(0f , Gdx.graphics.height/3f)
    private var limitX: Float = 0.0f
    private var limitY: Float = 0.0f
    private var moveToAction = MoveToAction()
    private var currentFrame: TextureRegion = handRegion

    init {
        this.x = initPosition.x
        this.y = initPosition.y
        setMoveAction()
    }

    override fun act(delta: Float) {
        super.act(delta)
        if(actions.isEmpty) setMoveAction()

        runTime += delta
    }

    private fun setMoveAction(){
        moveToAction = MoveToAction()
        limitX = -getLimit( 50)
        limitY = getLimit(50)
        moveToAction.setPosition(initPosition.x + limitX, initPosition.y + limitY)
        moveToAction.duration = 2f
        moveToAction.interpolation = Interpolation.smooth
        this.addAction(moveToAction)
    }

    private fun getLimit(max : Int = 50): Float{
        return random.nextInt(max).toFloat()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        currentFrame = handAnim.getKeyFrame(runTime)
/*        if(handAnim.isAnimationFinished(runTime)){
            currentFrame = when(random.nextInt(3)){
                2 -> {
                    runTime = 0f
                    handAnim.getKeyFrame(runTime)
                }
                else -> handRegion
            }
        }*/

        batch!!.draw(currentFrame, x, y,
                currentFrame.regionWidth.toFloat(), currentFrame.regionHeight.toFloat())
    }
}