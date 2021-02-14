/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.api

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.run.cookie.run.game.Config.Debug
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors

interface Physical{
    fun getBoundsRect() : Rectangle
    fun <T> collides(actor : T): Boolean where T : Actor, T : Physical {
        val bound = getBoundsRect()
        val boundActor = actor.getBoundsRect()
        if(bound.x + bound.width >= boundActor.x && boundActor.x < bound.x + bound.width && Intersector.overlaps(boundActor, bound)){
            return true
        }
        return false
    }

    fun debugCollidesIfEnable(batch: Batch?, manager: AssetManager){
        if(!Debug.COLLISIONS.state) return
        val texture = manager.get(Descriptors.environment)
        val bound = getBoundsRect()
        val region = texture.findRegion(Assets.EnvironmentAtlas.SHADOW)
        batch?.draw(region, bound.x, bound.y, bound.width, bound.height)
    }

    fun  <T> isAboveObject(actor : T): Boolean where T : Actor, T : Physical {
        val actorBound = actor.getBoundsRect()
        val bound = getBoundsRect()
        return bound.y > actorBound.y && bound.x >= actorBound.x && bound.x < actorBound.x + actorBound.width
    }

    fun  <T> isAfterObject(actor : T): Boolean where T : Actor, T : Physical {
        val tailObj = actor.getBoundsRect().x + actor.getBoundsRect().width
        return getBoundsRect().x >= tailObj && getBoundsRect().x < tailObj + 30f
    }

    fun  <T> getTop(actor : T): Float where T : Actor, T : Physical {
        val actorBound = actor.getBoundsRect()
        return actorBound.y + actorBound.height
    }

    fun  <T> getRight(actor : T): Float where T : Actor, T : Physical {
        val actorBound = actor.getBoundsRect()
        return actorBound.x + actorBound.width
    }
}