/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.api

import com.badlogic.gdx.scenes.scene2d.Actor

interface WallActor {
    var nextActor: Actor?
    var distance: Int
    var distancePastListener: () -> Unit
    fun resetState()
}