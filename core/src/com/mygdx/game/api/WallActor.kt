package com.mygdx.game.api

import com.badlogic.gdx.scenes.scene2d.Actor

interface WallActor {
    var nextActor: Actor?
    var distance: Int
    var distancePastListener: () -> Unit
    fun resetState()
}