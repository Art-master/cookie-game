package com.mygdx.game.world

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.actors.*
import com.mygdx.game.impl.Scrollable


class GameWorld(private val manager : AssetManager) {
    val stage = Stage(ScreenViewport())

    private val background = Background(manager)
    private val table = Table(manager)
    private val window = Window(manager)
    private val moon = Moon(manager, window)
    private val city = City(manager, window)
    private val sky = Sky(manager, window)
    private val flower = FlowerInPot(manager, window)
    private val cookie = Cookie(manager)
    private val shadow = Shadow(manager)
    private val cupboard = Cupboard(manager, window)
    private val score = Score(manager)
    private val hand = Hand(manager)
    private val items = TableItems(manager, table)
    val actors : Array<Actor> = Array.with(background, cupboard, shadow, sky, moon, city, window, flower, table, hand, cookie, score)

    init {
        actors.addAll(items.getActors())
        addActorsToStage()
    }

    private fun addActorsToStage(){
        for(actor in actors){
            stage.addActor(actor)
        }
    }


    private fun stopMove(){
        for (actor in actors){
            if(actor is Scrollable){
                actor.stopMove()
            }
        }
    }

    private fun startMove(){
        foreEachActor{
            if(it is Scrollable) it.runMove()
        }
    }

    private fun foreEachActor(callbackData: (Actor) -> Unit){
        for (actor in actors){
                callbackData.invoke(actor)
        }
    }
    fun update(delta: Float){
        stage.act(delta)
    }

}