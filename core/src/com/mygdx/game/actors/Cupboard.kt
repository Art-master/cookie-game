package com.mygdx.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import com.mygdx.game.ScreenConfig
import com.mygdx.game.data.AssetLoader
import com.mygdx.game.impl.Scrollable
import com.mygdx.game.impl.Scrolled
import java.util.*

class Cupboard(private val window : Window) : Actor(), Scrollable {
    private val rand = Random()
    private val cupBoardRegion = AssetLoader.cupboard
    private var leftDoorRegion = getDoorRegion()
    private var rightDoorRegion = getDoorRegion()

    private val startY = 400f
    private val startX =  200
    private val windowX2 = window.scroll.getX() + window.scroll.width

    var scroll = Scrolled(
            windowX2 + startX + leftDoorRegion.originalWidth,
            startY,
            cupBoardRegion.originalWidth + leftDoorRegion.originalWidth,
            cupBoardRegion.originalHeight,
            Scrolled.ScrollSpeed.LEVEL_1.value)

    private val randPositionX = Random(startX.toLong())

    private var upperThinksTextures = getTexturesArray()
    private var downThinksTextures = getTexturesArray()

    override fun act(delta: Float) {
        super.act(delta)
        scroll.update(delta)
        if(scroll.isScrolledLeft){
            val position = window.scroll.getX() + window.scroll.width + leftDoorRegion.originalWidth+ startX
            scroll.reset(position)
            leftDoorRegion = getDoorRegion()
            rightDoorRegion = getDoorRegion()
            upperThinksTextures = getTexturesArray()
            downThinksTextures = getTexturesArray()
        }
    }

    private fun calculatePosition(): Float {
        var position: Float
        do{
            position = windowX2 + leftDoorRegion.originalWidth + randPositionX.nextInt(600)
        } while (position < (ScreenConfig.widthGame + leftDoorRegion.originalWidth))
        return position
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(cupBoardRegion, scroll.getX(), scroll.getY(),
                cupBoardRegion.originalWidth.toFloat(),
                cupBoardRegion.originalHeight.toFloat())
        drawUtensil(batch)
        drawLeftDoor(batch)
        drawRightDoor(batch)
    }

    private fun drawUtensil(batch: Batch){
        val shellX = scroll.getX() + 25
        val shellDownY = scroll.getY() + 25
        for(element in upperThinksTextures){
            batch.draw(element.region, shellX + element.randX, shellDownY,
                    element.region.originalWidth.toFloat(),
                    element.region.originalHeight.toFloat())
        }

        val shellUpY = scroll.getY() + 225
        for(element in downThinksTextures){
            batch.draw(element.region, shellX + element.randX, shellUpY,
                    element.region.originalWidth.toFloat(),
                    element.region.originalHeight.toFloat())
        }

    }

    private fun drawLeftDoor(batch: Batch?){
        var x = scroll.getX()
        var y = scroll.getY()
        if(leftDoorRegion == AssetLoader.openDoor){
            x -= leftDoorRegion.originalWidth - 20
            y -= 42
        }
        batch!!.draw(leftDoorRegion, x, y,
                leftDoorRegion.originalWidth.toFloat(),
                leftDoorRegion.originalHeight.toFloat())
    }

    private fun drawRightDoor(batch: Batch?){
        var x = scroll.getX() + cupBoardRegion.originalWidth
        var y = scroll.getY()
        val width = -rightDoorRegion.originalWidth.toFloat()
        val height = rightDoorRegion.originalHeight.toFloat()
        if(rightDoorRegion == AssetLoader.closeDoor){
            x -= 21
        }else {
            y -= 40
            x += rightDoorRegion.originalWidth - 40
        }

        batch!!.draw(rightDoorRegion, x, y, width, height)
    }

    private fun getDoorRegion(): TextureAtlas.AtlasRegion{
        return when(rand.nextInt(3)){
            1 -> AssetLoader.openDoor
            2 -> AssetLoader.closeDoor
            else -> AssetLoader.openDoor
        }
    }

    private fun getTexturesArray() : Array<Utensil>{
        val array = Array<Utensil>()
        for(i in 1..rand.nextInt(8)){
            val utensil = Utensil(getUtensilRegion(rand.nextInt(5)), rand.nextInt(500))
            array.add(utensil)
        }
        return array
    }

    private fun getUtensilRegion(num : Int): TextureAtlas.AtlasRegion {
        return when(num){
            0 -> AssetLoader.jar1
            1 -> AssetLoader.jar2
            2 -> AssetLoader.jar3
            3 -> AssetLoader.salt
            4 -> AssetLoader.paper
            5 -> AssetLoader.cup
            else -> AssetLoader.jar1
        }
    }

    override fun getX() = scroll.getX()
    override fun getY() = scroll.getY()



    data class Utensil(val region: TextureAtlas.AtlasRegion, val randX: Int)

    override fun stopMove() {
        scroll.isStopMove = true
    }

    override fun runMove() {
        scroll.isStopMove = false
    }

}