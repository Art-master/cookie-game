package com.mygdx.game.api

import com.mygdx.game.Config

enum class Debug(var state: Boolean) {
    COLLISIONS(Config.DEBUG_COLLISIONS)
}