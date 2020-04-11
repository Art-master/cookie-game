package com.mygdx.game.api

interface Animated {
    fun animate(type: AnimationType, runAfter: Runnable = Runnable {  })
}