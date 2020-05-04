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
import com.mygdx.game.Config
import com.mygdx.game.managers.ScreenManager
import com.mygdx.game.managers.ScreenManager.Screens.*
import com.mygdx.game.actors.game.*
import com.mygdx.game.api.AnimationType
import com.mygdx.game.api.Callback
import com.mygdx.game.api.Scrollable
import com.mygdx.game.managers.AudioManager


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
    private val cookie = Cookie(manager, table.worktopY, Config.WIDTH_GAME/2)
    private val shadow = Shadow(manager)
    private val cupboard = Cupboard(manager, window)
    private val score = Score(manager)
    private val arm = Arm(manager, cookie)
    private val items = TableItems(manager, table, cookie)
    val actors : Array<Actor> = Array()

    private var touchable = true
    private var isGameOver = false

    init {
        actors.addAll(background, cupboard, shadow, sky, moon, city, window, flower, table)
        actors.addAll(items.getActors())
        actors.addAll(cookie, arm, score)

        addActorsToStage()
        startInitAnimation()
        stopMoveAllActors()
        cookie.runMove()
        changeScore()
        stage.addListener(object : ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if(touchable) cookie.startJumpForce()
                return super.touchDown(event, x, y, pointer, button)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                if(touchable) cookie.endJumpForce()
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
    private fun startInitAnimation(){
        cookie.animate(AnimationType.SHOW_ON_SCENE, Runnable {
            startMoveAllActors()
        })
        arm.animate(AnimationType.SHOW_ON_SCENE, Runnable {
            arm.startRepeatableMove()
        })
    }

    private fun changeScore(){
        items.getActors().forEach{controlScore(it)}
    }

    private fun controlScore(actor: RandomTableItem){
        actor.callbackGoThrough = object : Callback {
            override fun call() {
                AudioManager.play(AudioManager.Sound.SCORE)
                score.scoreNum++
            }
        }
    }


    private fun stopMoveAllActors(){
        for (actor in actors){
            if(actor is Scrollable){
                actor.stopMove()
            }
        }
    }

    private fun startMoveAllActors(){
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
        checkContactCookieAndHand()
    }

    private fun checkContactCookieAndHand(){
        if(arm.x + arm.width >= cookie.x && isGameOver.not()){
            isGameOver = true
            touchable = false
            stopMoveAllActors()
            arm.actions.clear()
            arm.animate(AnimationType.HIDE_FROM_SCENE, Runnable{
                ScreenManager.setScreen(GAME_OVER, score.scoreNum)
            })

            AudioManager.stopAll()
        }
    }

}