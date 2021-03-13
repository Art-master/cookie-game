/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game

import com.run.cookie.run.game.actors.game.TableItemsManager.*
import kotlin.math.abs

object Config {
    const val WIDTH_GAME = 1920f
    const val HEIGHT_GAME = 1080f
    const val SHADOW_ANIMATION_TIME_S = 1f
    const val BUTTONS_ANIMATION_TIME_S = 1f
    const val SOUNDS_FOLDER = "sounds/"

    //Cookie
    const val GRAVITY = -100f
    const val VELOCITY_JUMP = 100f
    const val MAX_JUMP_HEIGHT = 140

    //Kitchen items
    const val MIN_CUPBOARD_ITEMS_COUNT_ON_TABLE = 4

    enum class Debug(var state: Boolean, var info: Any = 0) {
        COLLISIONS(false),
        ALWAYS_SHOW_STORY(false),
        COOKIE_POSITION(false),
        PERIODIC_JUMP(false),
        EMPTY_TABLE(false),
        PLAY_SERVICES(false),
        ADS(false),
        CERTAIN_TABLE_ITEM(false, Item.BABY_COOKIES_BOX.index),
        MAX_SPEED(false),
        MIN_DISTANCE(false),
        MAX_RANDOM_ITEMS(false),
        PROFILER(false),
        FPS(false)
    }

    enum class Achievement(val score: Int) {
        SUNGLASSES(50),
        HAT(150),
        BOOTS(250),
        BELT(350),
        GUN(450),
        BULLETS(550),
        FINISH_GAME(623)
    }

    const val SPEED_INCREASE_STEP = -1.3f
    const val ITEMS_DISTANCE_INCREASE_STEP = 10 // For Game over == 600
    private const val NORMAL_SPEED = -200f
    private val maxSpeed = (Achievement.FINISH_GAME.score / 10) * SPEED_INCREASE_STEP
    val DEFAULT_SCROLL_SPEED = if (Debug.MAX_SPEED.state) NORMAL_SPEED + maxSpeed else -200f
    var currentScrollSpeed = DEFAULT_SCROLL_SPEED

    enum class ItemScrollSpeed(var calc: () -> Float) {
        NONE({ 0f }),
        LEVEL_1({ currentScrollSpeed }),
        LEVEL_2({ LEVEL_1.calc() * 3f }),
        SLOW_MOVE({ LEVEL_1.calc() / 1.3f }),
        FAST_MOVE({ abs(LEVEL_1.calc()) / 6.6f }),
        VERY_FAST_MOVE({ FAST_MOVE.calc() * 10f }),
        VERY_FAST_MOVE_BACK({ VERY_FAST_MOVE.calc() * -3f })
    }
}