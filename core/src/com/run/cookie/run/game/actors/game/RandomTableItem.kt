package com.run.cookie.run.game.actors.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.actors.game.cookie.Cookie
import com.run.cookie.run.game.api.*
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import com.run.cookie.run.game.managers.AudioManager
import com.run.cookie.run.game.managers.AudioManager.SoundApp
import java.util.*

class RandomTableItem(private val manager: AssetManager,
                      private val table: Table,
                      private val cookie: Cookie) : GameActor(), Scrollable, Physical, Animated {

    private val rand = Random()
    private val texture = manager.get(Descriptors.environment)
    private lateinit var region: TextureAtlas.AtlasRegion

    private var startBound = Rectangle()
    private var bound = Rectangle()

    private val screenWidth = Gdx.graphics.width.toFloat()

    private lateinit var scroller: HorizontalScroll

    private var jumpOnSound: AudioManager.Audio? = null
    var startAct = false
    var distanceUntil = 100
    var prevActor: RandomTableItem? = null
    var callback: Callback? = null

    var callbackGoThrough: Callback? = null
    var isScored = false

    var structure = Structure.NORMAL
        private set

    var isStopGeneration = false

    enum class Structure {
        NORMAL, STICKY, JELLY, ICE, SHARP
    }

    init {
        setRandomItem()
        resetScroller()
        updateCoordinates()
        updateBound()
    }

    fun isItemLeft() = scroller.isScrolledLeft

    override fun act(delta: Float) {
        super.act(delta)
        checkDistance()
        if (startAct) {
            if (scroller.isScrolledLeft && isStopGeneration.not()) {
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

    private fun checkDistance() {
        if (prevActor != null) {
            if (prevActor!!.startAct.not() && screenWidth - scroller.getTailX() >= distanceUntil) {
                prevActor!!.startAct = true
            }
        }
    }

    private fun resetScroller() {
        scroller = HorizontalScroll(screenWidth, y,
                region.originalWidth, region.originalHeight, Config.ItemScrollSpeed.LEVEL_2)
    }

    private fun updateBound() {
        bound.x = x + startBound.x
        bound.y = scroller.getY() + startBound.y
        bound.width = startBound.width
        bound.height = startBound.height
    }

    private fun setRandomItem() {
        val maxItemCount  = 21
        when (19) { //TODO in production only - rand.nextInt(maxItemCount)
            1 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.BOX1)
                val boundHeight = region.originalHeight.toFloat() - 25 - 20
                val boundWidth = region.originalWidth.toFloat() - 100 - 92
                startBound = Rectangle(92f, 25f, boundWidth, boundHeight)
                y = table.worktopY - 45f
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            2 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.BOX2)
                val boundHeight = region.originalHeight.toFloat() - 35 - 15
                val boundWidth = region.originalWidth.toFloat() - 90 - 32
                startBound = Rectangle(32f, 25f, boundWidth, boundHeight)
                y = table.worktopY - 35f
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            3 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.BOX3)
                val boundHeight = region.originalHeight.toFloat() - 70 - 23
                val boundWidth = region.originalWidth.toFloat() - 35 - 70
                startBound = Rectangle(70f, 70f, boundWidth, boundHeight)
                y = table.worktopY - 70
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            4 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.BOX4)
                val boundHeight = region.originalHeight.toFloat() - 20
                val boundWidth = region.originalWidth.toFloat() - 10 - 50
                startBound = Rectangle(50f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 30
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            5 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.MILK_BOX)
                val boundHeight = region.originalHeight.toFloat()
                val boundWidth = region.originalWidth.toFloat() - 95
                startBound = Rectangle(95f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 30
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            6 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.YOGURT_BOX)
                val boundHeight = region.originalHeight.toFloat()
                val boundWidth = region.originalWidth.toFloat() - 95
                startBound = Rectangle(95f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 30
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            7 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.TOMATO)
                val boundHeight = region.originalHeight.toFloat() - 40
                val boundWidth = region.originalWidth.toFloat() - 50
                startBound = Rectangle(40f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 30
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            8 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.APPLE)
                val boundHeight = region.originalHeight.toFloat() - 60
                val boundWidth = region.originalWidth.toFloat() - 80
                startBound = Rectangle(40f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 30
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            9 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.LIME)
                val boundHeight = region.originalHeight.toFloat() - 5
                val boundWidth = region.originalWidth.toFloat() - 80
                startBound = Rectangle(40f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 30
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            10 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.ORANGE)
                val boundHeight = region.originalHeight.toFloat() - 5
                val boundWidth = region.originalWidth.toFloat() - 80
                startBound = Rectangle(40f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 30
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            11 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.JAM)
                val boundWidth = region.originalWidth.toFloat()
                startBound = Rectangle(0f, 0f, boundWidth, 21f)
                y = table.worktopY - 20
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.STICKY
            }
            12 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.JAM2)
                val boundWidth = region.originalWidth.toFloat()
                startBound = Rectangle(0f, 0f, boundWidth, 21f)
                y = table.worktopY - 20
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.STICKY
            }
            13 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.JAM3)
                val boundWidth = region.originalWidth.toFloat()
                startBound = Rectangle(0f, 0f, boundWidth, 21f)
                y = table.worktopY - 20
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.STICKY
            }
            14 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.JAM4)
                val boundWidth = region.originalWidth.toFloat()
                startBound = Rectangle(0f, 0f, boundWidth, 21f)
                y = table.worktopY - 20
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.STICKY
            }
            15 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.JAM5)
                val boundWidth = region.originalWidth.toFloat()
                startBound = Rectangle(0f, 0f, boundWidth, 21f)
                y = table.worktopY - 20
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.STICKY
            }
            16 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.JELLY)
                val boundHeight = region.originalHeight.toFloat() - 35 - 15
                val boundWidth = region.originalWidth.toFloat() - 90 - 65
                startBound = Rectangle(65f, 25f, boundWidth, boundHeight)
                y = table.worktopY - 35f
                structure = Structure.JELLY
            }
            17 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.JAR_WITH_JAM)
                val boundHeight = region.originalHeight.toFloat()
                val boundWidth = region.originalWidth.toFloat() - 135
                startBound = Rectangle(135f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 30
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            18 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.JAR_WITH_JAM_2)
                val boundHeight = region.originalHeight.toFloat() - 5
                val boundWidth = region.originalWidth.toFloat() - 150
                startBound = Rectangle(10f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 30
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            19 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.ICE_PUDDLE)
                val boundWidth = region.originalWidth.toFloat()
                startBound = Rectangle(0f, 0f, boundWidth, 21f)
                y = table.worktopY - 20
                jumpOnSound = null
                structure = Structure.ICE
            }
            20 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.PUSHPIN)
                val boundWidth = region.originalWidth.toFloat() - 60
                val boundHeight = region.originalHeight.toFloat()
                startBound = Rectangle(40f, 0f, boundWidth, boundHeight)
                y = table.worktopY
                jumpOnSound = null
                structure = Structure.SHARP
            }
            21 -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.BREAD)
                val boundHeight = region.originalHeight.toFloat() - 20
                val boundWidth = region.originalWidth.toFloat() - 30 - 50
                startBound = Rectangle(50f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 30
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
            else -> {
                region = texture.findRegion(Assets.EnvironmentAtlas.BOX4)
                val boundHeight = region.originalHeight.toFloat() - 20
                val boundWidth = region.originalWidth.toFloat() - 10 - 50
                startBound = Rectangle(50f, 0f, boundWidth, boundHeight)
                y = table.worktopY - 30
                jumpOnSound = SoundApp.JUMP_ON_BOX
                structure = Structure.NORMAL
            }
        }
    }

    private fun updateCoordinates() {
        height = scroller.height.toFloat()
        width = scroller.width.toFloat()
        x = scroller.getX()
        y = scroller.getY()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val width = region.originalWidth.toFloat()
        val height = region.originalHeight.toFloat()
        if (startAct) batch!!.draw(region, x, y, x, y, width, height, scaleX, scaleY, rotation)
        debugCollidesIfEnable(batch, manager)
    }

    private fun isGoThrough(actor: Actor) {
        val actorMiddlePoint = actor.x + actor.width / 2
        val itemMiddlePoint = scroller.getTailX() - scroller.width / 2

        if (!isScored && actorMiddlePoint >= itemMiddlePoint) {
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

    override fun updateSpeed() {
        scroller.update()
    }

    fun jumpedOn() {
        if (jumpOnSound == null) return
        AudioManager.play(jumpOnSound!!)
    }

    override fun getBoundsRect() = bound

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = 0.1f
        val action = when (type) {
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