package com.mygdx.game.world

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.actors.game.*
import com.mygdx.game.impl.Callback
import com.mygdx.game.impl.Scrollable


class GameWorld(private val manager : AssetManager) {
    companion object{
        val gravity = Vector2(0f, 12f)
    }

    val stage = Stage(ScreenViewport())

    private val background = Background(manager)
    private val table = Table(manager, 240f)
    private val window = Window(manager, 270f)
    private val moon = Moon(manager, window)
    private val city = City(manager, window)
    private val sky = Sky(manager, window)
    private val flower = FlowerInPot(manager, window)
    private val cookie = Cookie(manager, table.worktopY)
    private val shadow = Shadow(manager)
    private val cupboard = Cupboard(manager, window)
    private val score = Score(manager)
    private val hand = Hand(manager)
    private val items = TableItems(manager, table, cookie)
    val actors : Array<Actor> = Array.with(background, cupboard, shadow, sky, moon, city, window, flower, table, hand, cookie, score)

    init {
        actors.addAll(items.getActors())
        addActorsToStage()
        changeScore()
        stage.addListener(object : ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                cookie.startJumpForce()
                return super.touchDown(event, x, y, pointer, button)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                cookie.endJumpForce()
                super.touchUp(event, x, y, pointer, button)
            }
        }
        )
        Gdx.input.inputProcessor = stage
    }

    private fun addActorsToStage(){
        for(actor in actors){
            stage.addActor(actor)
        }
    }

    private fun changeScore(){
        items.getActors().forEach{controlScore(it)}
    }

    private fun controlScore(actor: RandomTableItem){
        actor.callbackGoThrough = object : Callback {
            override fun call() {
                score.scoreNum++
            }
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