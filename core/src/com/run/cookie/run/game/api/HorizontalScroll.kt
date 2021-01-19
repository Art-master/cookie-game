package com.run.cookie.run.game.api

import com.badlogic.gdx.math.Vector2
import com.run.cookie.run.game.Config

open class HorizontalScroll(val originX: Float,
                            val originY: Float,
                            var width: Int,
                            var height: Int,
                            var scrollSpeed: Config.ItemScrollSpeed = Config.ItemScrollSpeed.NONE) {

    private val position = Vector2(originX, originY)
    private val velocity = Vector2(scrollSpeed.calc(), 0f)
    var isScrolledLeft = false
    var isStopMove = false

    open fun act(delta: Float) {
        if (!isStopMove) {
            position.add(velocity.x * delta, velocity.y * delta)
        }

        if (getTailX() < 0) isScrolledLeft = true
    }

    open fun reset(newX: Float = originX) {
        position.x = newX
        isScrolledLeft = false
        isStopMove = false
    }

    open fun getTailX() = position.x + width
    open fun getX() = position.x
    open fun setX(x: Float) {
        position.x = x
    }

    open fun getY() = position.y

    open fun update(x: Float = position.x, y: Float = position.y, width: Int = this.width,
                    height: Int = this.height, speed: Config.ItemScrollSpeed = scrollSpeed) {
        position.x = x
        position.y = y
        this.width = width
        this.height = height
        scrollSpeed = speed
        velocity.x = scrollSpeed.calc()
    }

}