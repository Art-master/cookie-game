/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.game.cookie

interface CookieLifecycle {
    fun jumpStart(){}
    fun jumpPeak(){}
    fun jumpEnd(isGround: Boolean){}
    fun update(delta: Float, cookie: Cookie) {}
    fun stumbled() {}
    fun caught() {}
}