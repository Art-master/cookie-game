package com.mygdx.game.world

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import com.mygdx.game.actors.*
import com.mygdx.game.impl.Scrollable


class GameWorld(cookie: Cookie, background: Background, city: City, cupboard: Cupboard,
                flowerInPot: FlowerInPot, moon: Moon, score: Score,
                shadow: Shadow, sky: Sky, table: Table, window: Window) {

    val actors : Array<Actor> = Array.with(cookie, background, city, cupboard,
            flowerInPot, moon, score, shadow, sky, table, window)

    public fun stopMove(){
        for (actor in actors){
            if(actor is Scrollable){
                actor.stopMove()
            }
        }
    }

    public fun startMove(){
        for (actor in actors){
            if(actor is Scrollable){
                actor.runMove()
            }
        }
    }
}