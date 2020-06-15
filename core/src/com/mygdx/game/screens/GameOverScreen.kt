package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.Config
import com.mygdx.game.managers.ScreenManager
import com.mygdx.game.managers.ScreenManager.Screens.*
import com.mygdx.game.actors.game_over_screen.GameOverTitle
import com.mygdx.game.actors.game_over_screen.MainMenuIcon
import com.mygdx.game.actors.game_over_screen.RestartIcon
import com.mygdx.game.actors.Shadow
import com.mygdx.game.actors.game_over_screen.Scores
import com.mygdx.game.actors.main_menu_screen.*
import com.mygdx.game.api.AnimationType.*
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.AudioManager.MusicApp.*
import com.mygdx.game.managers.AudioManager.Sound.*

class GameOverScreen(private val params: Array<out Any>) : Screen {

    private val manager: AssetManager = params.first { it is AssetManager } as AssetManager
    private val camera = OrthographicCamera(Config.WIDTH_GAME, Config.HEIGHT_GAME)
    private val stage = Stage(ScreenViewport(camera))

    init {
        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if(manager.isFinished && stage.actors.isEmpty){
            addActorsToStage()
        }
        stage.act(delta)
        stage.draw()
    }

    private fun addActorsToStage(){
        val background = Background(manager)
        val title = GameOverTitle(manager)
        val restartIcon = RestartIcon(manager)
        val mainMenuIcon = MainMenuIcon(manager)
        val shadow = Shadow(manager)
        val scores = initScoreActor()

        stage.apply {
            addActor(background)
            addActor(title)
            addActor(restartIcon)
            addActor(mainMenuIcon)
            addActor(shadow)
            addActor(scores)
        }

        title.animate(SHOW_ON_SCENE)
        restartIcon.animate(SHOW_ON_SCENE)
        mainMenuIcon.animate(SHOW_ON_SCENE)
        shadow.animate(SHOW_ON_SCENE)
        scores.animate(SHOW_ON_SCENE)
        AudioManager.play(MAIN_MENU_MUSIC)

        addClickListener(restartIcon) {
            AudioManager.stopAll()
            title.animate(HIDE_FROM_SCENE)
            restartIcon.animate(HIDE_FROM_SCENE)
            mainMenuIcon.animate(HIDE_FROM_SCENE)
            scores.animate(HIDE_FROM_SCENE)
            shadow.animate(HIDE_FROM_SCENE, Runnable {
                ScreenManager.setScreen(GAME_SCREEN, manager)
            })
        }

        addClickListener(mainMenuIcon) {
            title.animate(HIDE_FROM_SCENE)
            restartIcon.animate(HIDE_FROM_SCENE)
            mainMenuIcon.animate(HIDE_FROM_SCENE)
            scores.animate(HIDE_FROM_SCENE)
            shadow.animate(HIDE_FROM_SCENE, Runnable {
                ScreenManager.setScreen(START_SCREEN)
            })
        }
    }

    private fun initScoreActor(): Scores{
        var score = 0
        if(params.isEmpty().not()){
            val scoreRaw = params[1]
            score =if(scoreRaw is Int){
               scoreRaw
            } else 0
        }
        return Scores(score)
    }

    private fun addClickListener(actor: Actor, function: () -> Unit){
        actor.addListener(object: ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                AudioManager.play(CLICK_SOUND)
                function()
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        stage.dispose()
    }
}