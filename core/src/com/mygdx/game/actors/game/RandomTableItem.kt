package com.mygdx.game.actors.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.api.Callback
import com.mygdx.game.api.Physical
import com.mygdx.game.api.Scrollable
import com.mygdx.game.api.Scrolled
import java.util.*

class RandomTableItem(manager : AssetManager,
                      private val table : Table,
                      private val cookie : Cookie) : Actor(), Scrollable, Physical{

    private val rand = Random()
    private val texture = manager.get(Descriptors.environment)
    private var region = getRandomItemRegion()
    private var bound = Rectangle()
    private val screenWidth = Gdx.graphics.width.toFloat()

    private var scroller = Scrolled(screenWidth, table.worktopY,
            region.originalWidth, region.originalHeight,
            Scrolled.ScrollSpeed.LEVEL_2.value)

    var startAct = false
    var distanceUntil = 100
    var prevActor: RandomTableItem? = null
    var callback : Callback? = null

    var callbackGoThrough : Callback? = null
    var isScored = false
    val shape = ShapeRenderer()

    init {
        updateCoordinates()
        setBound()
    }

    override fun act(delta: Float) {
        super.act(delta)

        checkDistance()
        if(startAct){
            cookie.checkCollides(this)
            updateCoordinates()
            scroller.update(delta)
            setBound()
            isGoThrough(cookie)

            if(scroller.isScrolledLeft){
                startAct = false
                isScored = false
                changeRegion()
                resetScroller()
                callback!!.call()
            }
        }
    }

    private fun checkDistance(){
        if(prevActor != null){
            if(prevActor!!.startAct.not() && screenWidth - scroller.getTailX() >= distanceUntil){
                prevActor!!.startAct = true
            }
        }
    }

    fun changeRegion(){
        region = getRandomItemRegion()
    }

    private fun resetScroller(){
       scroller = Scrolled(screenWidth, table.worktopY,
                region.originalWidth, region.originalHeight,
                Scrolled.ScrollSpeed.LEVEL_2.value)
    }

    private fun setBound(){
        bound.x = x
        bound.y = y
        bound.width = width
        bound.height = height
    }

    private fun getRandomItemRegion(): TextureAtlas.AtlasRegion{
        return when(rand.nextInt(4)){
            1 -> texture.findRegion(Assets.EnvironmentAtlas.LIME)
            2 -> texture.findRegion(Assets.EnvironmentAtlas.APPLE)
            3 -> texture.findRegion(Assets.EnvironmentAtlas.MILK_BOX)
            4 -> texture.findRegion(Assets.EnvironmentAtlas.YOGURT_BOX)
            //1 -> texture.findRegion(Assets.EnvironmentAtlas.GLASS)
            //6 -> texture.findRegion(Assets.EnvironmentAtlas.PIE)
            //5 -> texture.findRegion(Assets.EnvironmentAtlas.CARROT)
            else -> texture.findRegion(Assets.EnvironmentAtlas.ORANGE)
        }
    }

    private fun updateCoordinates(){
        height = scroller.height.toFloat()
        width = scroller.width.toFloat()
        x = scroller.getX()
        y = scroller.getY()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if(startAct) batch!!.draw(region, x, y, region.originalWidth.toFloat(), region.originalHeight.toFloat())
/*        if(startAct){
            shape.setAutoShapeType(true)
            shape.begin()
            shape.rect(x, y, width, height)
            shape.end()
        }*/
    }

    private fun isGoThrough(actor: Actor) {
        val actorMiddlePoint = actor.x + actor.width/2
        val itemMiddlePoint = scroller.getTailX() -  scroller.width/2

        if(!isScored && actorMiddlePoint >= itemMiddlePoint){
            isScored = true
            callbackGoThrough?.call()
        }
    }

    override fun stopMove() {
        scroller.isStopMove = true
    }

    override fun runMove() {
        scroller.isStopMove = false
    }

    override fun getBoundsRect() = bound
}