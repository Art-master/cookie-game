package com.mygdx.game.actors

interface Animated {
    fun animate(isRevert: Boolean = false, runAfter: Runnable = Runnable {  })
}