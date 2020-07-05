package com.mygdx.game.world

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
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
import com.mygdx.game.actors.game.cookie.Cookie
import com.mygdx.game.actors.game.cookie.CookieShadow
import com.mygdx.game.actors.game.cookie.Hat
import com.mygdx.game.actors.game.cookie.Sunglasses
import com.mygdx.game.api.AnimationType
import com.mygdx.game.api.Callback
import com.mygdx.game.api.Scrollable
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.ScreenManager.Param.*
import com.mygdx.game.actors.Shadow as SceneShadow


class GameWorld(manager: AssetManager) {

    val stage = Stage(ScreenViewport())

    private val background = Background(manager)
    private val table = Table(manager, 240f)
    private val window = Window(manager, 270f)
    private val city = City(manager, window)
    private val flower = FlowerInPot(manager, window)
    private val cookie = Cookie(manager, table.worktopY, Config.WIDTH_GAME / 2)
    private val sunglasses = Sunglasses(manager, cookie)
    private val hat = Hat(manager, cookie)
    private val cookieShadow = CookieShadow(manager, cookie)
    private val shadow = Shadow(manager)
    private val cupboard = Cupboard(manager, window)
    private val score = Score(manager)
    private val arm = Arm(manager, cookie)
    private val items = TableItems(manager, table, cookie)
    private val sceneShadow = SceneShadow(manager)
    val actors: Array<Actor> = Array()

    private var touchable = true
    private var isGameOver = false

    init {
        actors.addAll(background, cupboard, shadow, city, window, flower, table)
        actors.addAll(items.getActors())
        actors.addAll(cookieShadow, cookie, sunglasses, hat, arm, score, sceneShadow)

        addActorsToStage()
        stopMoveAllActors()
        startInitAnimation()
        cookie.runMove()
        changeScore()
        stage.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (touchable) cookie.startJumpForce()
                return super.touchDown(event, x, y, pointer, button)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                if (touchable) cookie.endJumpForce()
                super.touchUp(event, x, y, pointer, button)
            }
        }
        )
        Gdx.input.inputProcessor = stage
    }

    private fun addActorsToStage() {
        for (actor in actors) {
            stage.addActor(actor)
        }
    }

    private fun startInitAnimation() {
        sceneShadow.animate(AnimationType.SHOW_ON_SCENE, Runnable {
            cookie.animate(AnimationType.SHOW_ON_SCENE, Runnable {
                startMoveAllActors()
            })
            arm.animate(AnimationType.SHOW_ON_SCENE, Runnable {
                arm.startRepeatableMove()
            })
        })
    }

    private fun changeScore() {
        items.getActors().forEach { controlScore(it) }
    }

    private fun controlScore(actor: RandomTableItem) {
        actor.callbackGoThrough = object : Callback {
            override fun call() {
                score.scoreNum++
                if(score.scoreNum == 1){
                    sunglasses.animate(AnimationType.SHOW_ON_SCENE)
                } else if (score.scoreNum == 3){
                    hat.animate(AnimationType.SHOW_ON_SCENE)
                }
            }
        }
    }


    private fun stopMoveAllActors() {
        for (actor in actors) {
            if (actor is Scrollable && actor !is Cookie) {
                actor.stopMove()
            }
        }
    }

    private fun startMoveAllActors() {
        foreEachActor {
            if (it is Scrollable) it.runMove()
        }
    }

    private fun foreEachActor(callbackData: (Actor) -> Unit) {
        for (actor in actors) {
            callbackData.invoke(actor)
        }
    }

    fun update(delta: Float) {
        stage.act(delta)
        checkContactCookieAndHand()
    }

    private fun checkContactCookieAndHand() {
        if (arm.x + arm.width >= cookie.x && isGameOver.not()) {
            isGameOver = true
            touchable = false
            stopMoveAllActors()
            arm.actions.clear()
            arm.animate(AnimationType.COOKIE_CATCH, Runnable {
                sunglasses.animate(AnimationType.HIDE_FROM_SCENE)
                hat.animate(AnimationType.HIDE_FROM_SCENE)
                arm.animate(AnimationType.HIDE_FROM_SCENE, Runnable {
                    ScreenManager.setScreen(GAME_OVER, Pair(SCORE, score.scoreNum))
                })
            })

            AudioManager.stopAll()
        }
    }

}