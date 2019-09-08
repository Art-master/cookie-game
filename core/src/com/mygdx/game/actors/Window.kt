package com.mygdx.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.impl.Scrollable
import com.mygdx.game.data.AssetLoader
import com.mygdx.game.impl.Listener
import com.mygdx.game.impl.Scrolled
import java.util.*
import kotlin.collections.ArrayList

class Window : Actor(), Scrollable {

    private val rand = Random()
    private val region = AssetLoader.window

    private val curtainLeft = AssetLoader.curtainLeft
    private val curtainRight = AssetLoader.curtainRight
    private var curtainColor = getRandomColor()

    var scroll = Scrolled(
            Gdx.graphics.width.toFloat(), 80f,
            AssetLoader.window.originalWidth,
            AssetLoader.window.originalHeight,
            Scrolled.ScrollSpeed.LEVEL_1.value)

    private val listeners = ArrayList<Listener>()

    override fun act(delta: Float) {
        super.act(delta)
        scroll.update(delta)
        if(scroll.isScrolledLeft){
            scroll.reset(scroll.originX)
            curtainColor = getRandomColor()
            callListeners()
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.GRAY
        batch.draw(region, scroll.getX(), scroll.getY(),
                scroll.width.toFloat(), scroll.height.toFloat())

        //batch.color = curtainColor
        batch.draw(curtainLeft, scroll.getX(), scroll.getY(),
                curtainLeft.originalWidth.toFloat(), curtainLeft.originalHeight.toFloat())

        batch.draw(curtainRight, scroll.getX() + scroll.width - curtainRight.originalWidth, scroll.getY(),
                curtainLeft.originalWidth.toFloat(), curtainLeft.originalHeight.toFloat())
        batch.color = Color.GRAY
    }

    private fun getRandomColor(): Color {
        return when(rand.nextInt(17)){
            0 -> Color.OLIVE
            1 -> Color.BROWN
            2 -> Color.CORAL
            3 -> Color.FIREBRICK
            4 -> Color.CYAN
            5 -> Color.FOREST
            6 -> Color.GOLD
            7 -> Color.GOLDENROD
            8 -> Color.LIME
            9 -> Color.MAROON
            10 -> Color.PINK
            11 -> Color.ROYAL
            12 -> Color.SCARLET
            13 -> Color.SLATE
            14 -> Color.TAN
            15 -> Color.TEAL
            16 -> Color.VIOLET
            17 -> Color.SALMON
            else -> Color.WHITE
        }
    }

    override fun getX() = scroll.getX()
    override fun getY() = scroll.getY()
    fun getWindowsillY() = scroll.getY() + 160
    fun getWindowsillX() = scroll.getX() + 280

    fun addResetListener(listener: Listener){
        listeners.add(listener)
    }

    private fun callListeners(){
        listeners.forEach{it.call()}
    }

    fun isCoordXInUse(x: Float): Boolean {
        return x >= scroll.getX() && x <= scroll.getX() + region.originalWidth
    }

    override fun stopMove() {
       scroll.isStopMove = true
    }

    override fun runMove() {
        scroll.isStopMove = false
    }
}