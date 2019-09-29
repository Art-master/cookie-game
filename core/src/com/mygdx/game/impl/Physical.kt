package com.mygdx.game.impl

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor

interface Physical{
    fun getBoundsRect() : Rectangle
    fun <T> collides(actor : T): Boolean where T : Actor, T : Physical{
        val rect = getBoundsRect()
        if(actor.x + actor.width >= rect.x && Intersector.overlaps(actor.getBoundsRect(), rect)){
            return true
        }
        return false
    }
}