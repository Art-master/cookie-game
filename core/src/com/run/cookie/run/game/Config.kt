package com.run.cookie.run.game

import kotlin.math.abs

object Config {
    const val WIDTH_GAME = 1920f
    const val HEIGHT_GAME = 1080f
    const val SHADOW_ANIMATION_TIME_S = 1f
    const val BUTTONS_ANIMATION_TIME_S = 1f
    const val SOUNDS_FOLDER = "sounds/"

    //Cookie
    const val GRAVITY = -50f
    const val VELOCITY_JUMP = 50f
    const val MAX_JUMP_HEIGHT = 200

    //Kitchen items
    const val MIN_CUPBOARD_ITEMS_COUNT_ON_TABLE = 4
    const val CUPBOARD_ITEMS_COUNT = 20

    enum class Debug(var state: Boolean, var info: Any = 0) {
        COLLISIONS(false),
        ALWAYS_SHOW_STORY(false),
        COOKIE_POSITION(false),
        PERIODIC_JUMP(false),
        EMPTY_TABLE(false),
        PLAY_SERVICES(false),
        ADS(false),
        CERTAIN_TABLE_ITEM(false, 20),
    }

    enum class Achievement(val score: Int) {
        SUNGLASSES(50),
        HAT(100),
        BOOTS(150),
        BELT(200),
        GUN(250),
        BULLETS(300),
        FINISH_GAME(400)
    }

    const val DEFAULT_SCROLL_SPEED = -200f
    var currentScrollSpeed = DEFAULT_SCROLL_SPEED
    const val SPEED_INCREASE_STEP = -5f

    enum class ItemScrollSpeed(var calc: () -> Float) {
        NONE({ 0f }),
        LEVEL_1({ currentScrollSpeed }),
        LEVEL_2({ LEVEL_1.calc() * 3f }),
        SLOW_MOVE({ LEVEL_1.calc() / 1.1f }),
        FAST_MOVE({ abs(LEVEL_1.calc()) / 6.6f }),
        VERY_FAST_MOVE({ FAST_MOVE.calc() * 10f }),
        VERY_FAST_MOVE_BACK({ VERY_FAST_MOVE.calc() * -3f })
    }
}