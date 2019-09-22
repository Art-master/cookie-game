package com.mygdx.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.impl.Scrollable
import com.mygdx.game.impl.Scrolled
import java.util.*

class RandomTableItem(manager : AssetManager, table : Table, var prevItem : RandomTableItem?) : Actor(), Scrollable{
    private val rand = Random()
    private val texture = manager.get(Descriptors.environment)
    private var region = getRandomItemRegion()
    private val screenWidth = Gdx.graphics.width.toFloat()
    private val screenHeight = Gdx.graphics.height.toFloat()
    private var scroller = Scrolled(screenWidth, table.worktopY,
            region.originalWidth, region.originalHeight,
            Scrolled.ScrollSpeed.LEVEL_2.value)

    private val diff = 30f
    private var isShowing = false

    init {
        updateCoordinates()
    }

    override fun act(delta: Float) {
        super.act(delta)
        updateCoordinates()

        val d = prevItem!!.x == screenWidth
        if(d){
            scroller.update(delta)
            isShowing = true
        }else isShowing = false

        if(scroller.isScrolledLeft) scroller.reset(screenWidth)
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
         if(isShowing){
             batch!!.draw(region, x, y, region.originalWidth.toFloat(), region.originalHeight.toFloat())
         }
    }

    override fun stopMove() {

    }

    override fun runMove() {
    }
}