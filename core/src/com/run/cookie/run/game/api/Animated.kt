package com.run.cookie.run.game.api

interface Animated {
    fun animate(type: AnimationType, runAfter: Runnable = Runnable {  })
}