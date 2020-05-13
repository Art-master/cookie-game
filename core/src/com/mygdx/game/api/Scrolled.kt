package com.mygdx.game.api

import com.badlogic.gdx.math.Vector2

open class Scrolled(val originX: Float,
                    val originY: Float,
                    var width: Int,
                    var height: Int,
                    var scrollSpeed: ScrollSpeed = ScrollSpeed.NONE) {

    private val position = Vector2(originX, originY)
    private val velocity = Vector2(scrollSpeed.value, 0f)
    var isScrolledLeft = false
    var isStopMove = false

    open fun update(delta: Float) {
        if(!isStopMove) position.add(velocity.cpy().scl(delta))

        if (getTailX() < 0) isScrolledLeft = true
    }
    open fun reset(newX: Float) {
        position.x = newX
        isScrolledLeft = false
    }

    open fun getTailX() = position.x + width
    open fun getX() = position.x
    open fun setX(x: Float) {
        position.x = x
    }
    open fun getY() = position.y

    open fun update(x: Float = position.x, y: Float = position.y, width: Int = this.width,
                    height: Int = this.height, speed: ScrollSpeed = ScrollSpeed.NONE){
        position.x = x
        position.y = y
        this.width = width
        this.height = height
        scrollSpeed = speed
        velocity.x = scrollSpeed.value
    }

    enum class ScrollSpeed(val value: Float) {
        NONE(0f),
        LEVEL_1(-200f),//50
        LEVEL_2(LEVEL_1.value * 3),
        SLOW_MOVE(LEVEL_1.value / 1.1f),
        FAST_MOVE(30f)
    }
}