package com.mygdx.game.api

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

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

    fun debugIfEnable(batch: Batch?, manager: AssetManager){
        if(!GameSettings.DEBUG.state) return
        val texture = manager.get(Descriptors.environment)
        val bound = getBoundsRect()
        batch?.draw(texture.findRegion(Assets.EnvironmentAtlas.SHADOW), bound.x, bound.y, bound.width, bound.height)
    }
}