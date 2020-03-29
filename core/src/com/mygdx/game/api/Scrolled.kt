package com.mygdx.game.api

import com.badlogic.gdx.math.Vector2

open class Scrolled (val originX: Float, val originY: Float, val width: Int, val height: Int, scrollSpeed: Float) {
    private val position = Vector2(originX, originY)
    private val velocity = Vector2(scrollSpeed, 0f)
    var isScrolledLeft = false
    var isStopMove = false

    open fun update(delta: Float) {
        if(!isStopMove) position.add(velocity.cpy().scl(delta))

        if (position.x + width < 0) isScrolledLeft = true
    }
    open fun reset(newX: Float) {
        position.x = newX
        isScrolledLeft = false
    }

    open fun getTailX() = position.x + width
    open fun getX() = position.x
    open fun getY() = position.y

    enum class ScrollSpeed(val value: Float) {
        LEVEL_1(-50f),//50
        LEVEL_2(LEVEL_1.value * 3)
    }
}