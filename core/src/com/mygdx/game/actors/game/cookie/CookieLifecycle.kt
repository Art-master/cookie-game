package com.mygdx.game.actors.game.cookie

interface CookieLifecycle {
    fun jumpStart(){}
    fun jumpPeak(){}
    fun jumpEnd(isGround: Boolean){}
    fun update(delta: Float, cookie: Cookie) {}
    fun stumbled() {}
    fun caught() {}
}