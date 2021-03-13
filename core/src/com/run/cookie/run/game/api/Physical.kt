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

interface Physical {
    fun getBoundsRect(): Rectangle
    fun <T> collides(actor: T): Boolean where T : Actor, T : Physical {
        val main = getBoundsRect()
        val second = actor.getBoundsRect()
        return Intersector.overlaps(second, main)
    }

    fun debugCollidesIfEnable(batch: Batch?, manager: AssetManager) {
        if (!Debug.COLLISIONS.state) return
        val texture = manager.get(Descriptors.environment)
        val bound = getBoundsRect()
        val region = texture.findRegion(Assets.EnvironmentAtlas.SHADOW)
        batch?.draw(region, bound.x, bound.y, bound.width, bound.height)
    }

    fun <T> isAboveObject(actor: T): Boolean where T : Actor, T : Physical {
        val actorBound = actor.getBoundsRect()
        val bound = getBoundsRect()
        return bound.y > actorBound.y + actorBound.height
    }

    fun <T> isStepOnObject(actor: T): Boolean where T : Actor, T : Physical {
        val actorBound = actor.getBoundsRect()
        val bound = getBoundsRect()
        return getBoundsTail() > actorBound.x && bound.y >= actor.getBoundsTop() - 30
    }

    fun getBoundsTail() = getBoundsRect().x + getBoundsRect().width
    fun getBoundsTop() = getBoundsRect().y + getBoundsRect().height


    fun <T> inBoundaries(actor: T): Boolean where T : Actor, T : Physical {
        val actorBound = actor.getBoundsRect()
        val main = getBoundsRect()
        return getBoundsTail() >= actorBound.x && main.x <= getRight(actor)
    }


    fun <T> isAfterObject(actor: T): Boolean where T : Actor, T : Physical {
        val tailObj = actor.getBoundsRect().x + actor.getBoundsRect().width
        return getBoundsRect().x in tailObj..tailObj + 30f
    }

    fun <T> getTop(actor: T): Float where T : Actor, T : Physical {
        val actorBound = actor.getBoundsRect()
        return actorBound.y + actorBound.height
    }

    fun <T> getRight(actor: T): Float where T : Actor, T : Physical {
        val actorBound = actor.getBoundsRect()
        return actorBound.x + actorBound.width
    }
}