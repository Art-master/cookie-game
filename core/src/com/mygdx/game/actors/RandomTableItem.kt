package com.mygdx.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.impl.Callback
import com.mygdx.game.impl.Scrollable
import com.mygdx.game.impl.Scrolled
import java.util.*

class RandomTableItem(manager : AssetManager, private val table : Table) : Actor(), Scrollable{
    private val rand = Random()
    private val texture = manager.get(Descriptors.environment)
    private var region = getRandomItemRegion()
    private val screenWidth = Gdx.graphics.width.toFloat()

    private var scroller = Scrolled(screenWidth, table.worktopY,
            region.originalWidth, region.originalHeight,
            Scrolled.ScrollSpeed.LEVEL_2.value)

    var startAct = false
    var distanceUntil = 100
    var prevActor: RandomTableItem? = null
    var callback : Callback? = null

    init {
        updateCoordinates()
    }

    override fun act(delta: Float) {
        checkDistance()

        super.act(delta)
        updateCoordinates()
        if(startAct){
            scroller.update(delta)
            if(scroller.isScrolledLeft){
                startAct = false
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

    private fun getRandomItemRegion(): TextureAtlas.AtlasRegion{
        return when(rand.nextInt(7)){
            1 -> texture.findRegion(Assets.EnvironmentAtlas.GLASS)
            2 -> texture.findRegion(Assets.EnvironmentAtlas.ORANGE)
            3 -> texture.findRegion(Assets.EnvironmentAtlas.LIME)
            4 -> texture.findRegion(Assets.EnvironmentAtlas.APPLE)
            5 -> texture.findRegion(Assets.EnvironmentAtlas.CARROT)
            6 -> texture.findRegion(Assets.EnvironmentAtlas.PIE)
            7 -> texture.findRegion(Assets.EnvironmentAtlas.MILK_BOX)
            8 -> texture.findRegion(Assets.EnvironmentAtlas.YOGURT_BOX)
            else -> texture.findRegion(Assets.EnvironmentAtlas.ORANGE)
        }
    }

    private fun updateCoordinates(){
        x = scroller.getX()
        y = scroller.getY()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(region, x, y, region.originalWidth.toFloat(), region.originalHeight.toFloat())
    }

    override fun stopMove() {

    }

    override fun runMove() {
    }
}