package com.run.cookie.run.game.api

interface Scrollable {
    fun stopMove()
    fun runMove()
    fun updateSpeed()
    fun isScrolled() = false
}