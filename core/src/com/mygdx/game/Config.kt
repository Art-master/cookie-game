package com.mygdx.game

object Config {

    //DEBUG
    const val DEBUG_COLLISIONS = false

    const val WIDTH_GAME = 1920f
    const val HEIGHT_GAME = 1080f
    const val SHADOW_ANIMATION_TIME_S = 1f
    const val BUTTONS_ANIMATION_TIME_S = 1f
    const val VIBRATION_TIME_MS = 100
    const val SOUNDS_FOLDER = "sounds/"

    enum class Achievement(val score: Int) {
        SUNGLASSES(5),
        HAT(10),
        BOOTS(15),
        BELT(20),
        GUN(25),
        BULLETS(30),
        FINISH_GAME(31)
    }
}