package com.mygdx.game.actors.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.game.Config
import com.mygdx.game.api.GameActor
import com.mygdx.game.api.HorizontalScroll
import com.mygdx.game.api.Listener
import com.mygdx.game.api.Scrollable
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import java.util.*
import kotlin.collections.ArrayList

class Window(manager: AssetManager, startY: Float) : GameActor(), Scrollable {
    private val texture = manager.get(Descriptors.environment)
    private val region = texture.findRegion(Assets.EnvironmentAtlas.WINDOW)

    private val rand = Random()

    private val curtainArtRegions = texture.findRegions(Assets.EnvironmentAtlas.CURTAIN_LEFT_ART)
    private var curtainRegion = curtainArtRegions.first()

    private var curtainColor = getRandomColor()

    var scroll = HorizontalScroll(
            Gdx.graphics.width.toFloat(), startY,
            region.originalWidth,
            region.originalHeight,
            Config.ItemScrollSpeed.LEVEL_1)

    private val listeners = ArrayList<Listener>()

    override fun act(delta: Float) {
        super.act(delta)
        scroll.update(delta)
        if (scroll.isScrolledLeft) {
            scroll.reset(scroll.originX)
            curtainColor = getRandomColor().lerp(Color.GRAY, 0.5f)
            callListeners()
            chooseCurtain()
        }
    }

    private fun chooseCurtain() {
        val num = rand.nextInt(curtainArtRegions.size)
        curtainRegion = curtainArtRegions[num]
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.GRAY
        batch.draw(region, scroll.getX(), scroll.getY(),
                scroll.width.toFloat(), scroll.height.toFloat())

        batch.color = curtainColor
        drawLeftCurtain(batch)
        drawRightCurtain(batch)
        batch.color = Color.GRAY
    }

    private fun drawLeftCurtain(batch: Batch) {
        val x = scroll.getX() - 80
        val y = scroll.getY() - 60
        val width = curtainRegion.originalWidth.toFloat()
        val height = curtainRegion.originalHeight.toFloat()
        batch.draw(curtainRegion, x, y, width, height)
    }

    private fun drawRightCurtain(batch: Batch) {
        val x = scroll.getTailX() + 40
        val y = scroll.getY() - 60
        val width = -curtainRegion.originalWidth.toFloat()
        val height = curtainRegion.originalHeight.toFloat()
        batch.draw(curtainRegion, x, y, width, height)
    }

    private fun getRandomColor(): Color {
        return when (rand.nextInt(17)) {
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
    fun getWindowsillY() = scroll.getY() + 90
    fun getWindowsillX() = scroll.getX() + 70

    fun addResetListener(listener: Listener) {
        listeners.add(listener)
    }

    private fun callListeners() {
        listeners.forEach { it.call() }
    }

    override fun stopMove() {
        scroll.isStopMove = true
    }

    override fun runMove() {
        scroll.isStopMove = false
    }

    override fun updateSpeed() {
        scroll.update()
    }
}