package com.mygdx.game

import kotlin.math.abs

object Config {
    const val WIDTH_GAME = 1920f
    const val HEIGHT_GAME = 1080f
    const val SHADOW_ANIMATION_TIME_S = 1f
    const val BUTTONS_ANIMATION_TIME_S = 1f
    const val VIBRATION_TIME_MS = 100
    const val SOUNDS_FOLDER = "sounds/"

    //Cookie
    const val GRAVITY = -50f
    const val VELOCITY_JUMP = 50f
    const val MAX_JUMP_HEIGHT = 200

    enum class Debug(var state: Boolean) {
        COLLISIONS(false),
        ALWAYS_SHOW_STORY(false)
    }

    enum class Achievement(val score: Int) {
        SUNGLASSES(1),
        HAT(3),
        BOOTS(6),
        BELT(9),
        GUN(12),
        BULLETS(15),
        FINISH_GAME(17)
    }

    const val DEFAULT_SCROLL_SPEED = -200f
    var currentScrollSpeed = DEFAULT_SCROLL_SPEED
    const val SPEED_INCREASE_STEP = -20f

    enum class ItemScrollSpeed(var calc: () -> Float) {
        NONE({ 0f }),
        LEVEL_1({ currentScrollSpeed }),
        LEVEL_2({ LEVEL_1.calc() * 3f }),
        SLOW_MOVE({ LEVEL_1.calc() / 1.1f }),
        FAST_MOVE({ abs(LEVEL_1.calc()) / 6.6f }),
        VERY_FAST_MOVE({ FAST_MOVE.calc() * 10f })
    }
}