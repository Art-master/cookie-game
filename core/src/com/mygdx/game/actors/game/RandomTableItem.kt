package com.mygdx.game.actors.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.api.*
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.AudioManager.Sound
import java.util.*

class RandomTableItem(manager : AssetManager,
                      private val table : Table,
                      private val cookie : Cookie) : GameActor(), Scrollable, Physical, Animated{

    private val rand = Random()
    private val texture = manager.get(Descriptors.environment)
    private lateinit var region: TextureAtlas.AtlasRegion

    private var startBound = Rectangle()
    private var bound = Rectangle()

    private val screenWidth = Gdx.graphics.width.toFloat()

    private lateinit var scroller: Scrolled

    private var jumpOnSound = Sound.NONE
    var startAct = false
    var distanceUntil = 100
    var prevActor: RandomTableItem? = null
    var callback : Callback? = null

    var callbackGoThrough : Callback? = null
    var isScored = false
    val shape = ShapeRenderer()

    init {
        setRandomItem()
        resetScroller()
        updateCoordinates()
        updateBound()
    }

    override fun act(delta: Float) {
        super.act(delta)
        checkDistance()
        if(startAct){
            if(scroller.isScrolledLeft){
                startAct = false
                isScored = false
                setRandomItem()
                resetScroller()
                callback!!.call()
            }
            cookie.checkCollides(this)
            updateCoordinates()
            scroller.update(delta)
            updateBound()
            isGoThrough(cookie)
        }
    }

    private fun checkDistance(){
        if(prevActor != null){
            if(prevActor!!.startAct.not() && screenWidth - scroller.getTailX() >= distanceUntil){
                prevActor!!.startAct = true
            }
        }
    }

    private fun resetScroller(){
       scroller = Scrolled(screenWidth, y,
                region.originalWidth, region.originalHeight, Scrolled.ScrollSpeed.LEVEL_2)
    }

    private fun updateBound(){
        bound.x = x + startBound.x
        bound.y = scroller.getY() + startBound.y
        bound.width = startBound.width
        bound.height = startBound.height
    }

    private fun setRandomItem(){
        when(rand.nextInt(13)){
            1 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.BOX1)
                val boundHeight = region.originalHeight.toFloat() - 25 - 20
                val boundWidth = region.originalWidth.toFloat() - 100 - 92
                startBound = Rectangle(92f, 25f, boundWidth, boundHeight)
                y = table.worktopY - 25f
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            2 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.BOX2)
                val boundHeight = region.originalHeight.toFloat() - 35 - 15
                val boundWidth = region.originalWidth.toFloat() - 90 - 32
                startBound = Rectangle(32f, 25f, boundWidth, boundHeight)
                y = table.worktopY - 35f
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            3 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.BOX3)
                val boundHeight = region.originalHeight.toFloat() - 70 - 23
                val boundWidth = region.originalWidth.toFloat() - 35
                startBound = Rectangle(0f, 70f, boundWidth, boundHeight)
                y = table.worktopY - 70
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            4 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.BOX4)
                val boundHeight = region.originalHeight.toFloat() - 20
                val boundWidth = region.originalWidth.toFloat() - 10
                startBound = Rectangle(0f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 25
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            5 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.MILK_BOX)
                val boundHeight = region.originalHeight.toFloat()
                val boundWidth = region.originalWidth.toFloat()
                startBound = Rectangle(0f, 0f, boundWidth, boundHeight)
                y = table.worktopY
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            6 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.YOGURT_BOX)
                val boundHeight = region.originalHeight.toFloat()
                val boundWidth = region.originalWidth.toFloat()
                startBound = Rectangle(0f, 0f, boundWidth, boundHeight)
                y = table.worktopY
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            7 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.TOMATO)
                val boundHeight = region.originalHeight.toFloat() -40
                val boundWidth = region.originalWidth.toFloat() -50
                startBound = Rectangle(40f, 0f, boundWidth, boundHeight)
                y = table.worktopY -20
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            8 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.APPLE)
                val boundHeight = region.originalHeight.toFloat() -60
                val boundWidth = region.originalWidth.toFloat() -80
                startBound = Rectangle(40f, 0f, boundWidth, boundHeight)
                y = table.worktopY -20
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            9 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.LIME)
                val boundHeight = region.originalHeight.toFloat() -5
                val boundWidth = region.originalWidth.toFloat() -80
                startBound = Rectangle(40f, 0f, boundWidth, boundHeight)
                y = table.worktopY -20
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            10 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.ORANGE)
                val boundHeight = region.originalHeight.toFloat() -5
                val boundWidth = region.originalWidth.toFloat() -80
                startBound = Rectangle(40f, 0f, boundWidth, boundHeight)
                y = table.worktopY -20
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            11-> {
                region = texture.findRegion(Assets.EnvironmentAtlas.JAM)
                val boundWidth = region.originalWidth.toFloat()
                startBound = Rectangle(0f, 0f, boundWidth, 21f)
                y = table.worktopY -20
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            12-> {
                region = texture.findRegion(Assets.EnvironmentAtlas.JAM2)
                val boundWidth = region.originalWidth.toFloat()
                startBound = Rectangle(0f, 0f, boundWidth, 21f)
                y = table.worktopY -20
                jumpOnSound = Sound.JUMP_ON_BOX
            }
            //1 -> texture.findRegion(Assets.EnvironmentAtlas.GLASS)
            else -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.BOX2)
                val boundHeight = region.originalHeight.toFloat() - 35 - 15
                val boundWidth = region.originalWidth.toFloat() - 90 - 32
                startBound = Rectangle(32f, 25f, boundWidth, boundHeight)
                y = table.worktopY - 35f
            }
        }
    }

    private fun updateCoordinates(){
        height = scroller.height.toFloat()
        width = scroller.width.toFloat()
        x = scroller.getX()
        y = scroller.getY()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val width = region.originalWidth.toFloat()
        val height = region.originalHeight.toFloat()
        if(startAct) batch!!.draw(region, x, y, x,y,  width, height, scaleX, scaleY, rotation)
/*        if(startAct){
            shape.setAutoShapeType(true)
            shape.begin()
            shape.rect(bound.x, bound.y, bound.width, bound.height)
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

    fun jumpedOn(){
        if(jumpOnSound == Sound.NONE) return
        AudioManager.play(jumpOnSound)
    }

    override fun getBoundsRect() = bound

    fun isSticky():Boolean {
        return region.name == Assets.EnvironmentAtlas.JAM ||
                region.name == Assets.EnvironmentAtlas.JAM2
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = 0.1f
        val action = when(type) {
            AnimationType.ITEM_SQUASH -> {
                val act1 = Actions.scaleTo(0.995f, 1f, animDuration, Interpolation.exp10)
                val act2 = Actions.scaleTo(1f, 1f, animDuration, Interpolation.exp10)
                Actions.sequence(act1, act2)
            }
            else -> return
        }
        addAction(action)
    }
}