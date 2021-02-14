/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.api

interface Scrollable {
    fun stopMove()
    fun runMove()
    fun updateSpeed()
    fun isScrolled() = false
}