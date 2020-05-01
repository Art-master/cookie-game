package com.mygdx.game.api

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor

interface Physical{
    fun getBoundsRect() : Rectangle
    fun <T> collides(actor : T): Boolean where T : Actor, T : Physical{
        val bound = getBoundsRect()
        val boundActor = actor.getBoundsRect()
        if(boundActor.x + boundActor.width >= bound.x && Intersector.overlaps(boundActor, bound)){
            return true
        }
        return false
    }
}