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
import com.mygdx.game.actors.main_menu_screen.*
import com.mygdx.game.data.Descriptors
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.AudioManager.MusicApp.*
import com.mygdx.game.managers.AudioManager.Sounds.*

class GameOverScreen : Screen {
    private val manager = AssetManager()
    private val camera = OrthographicCamera(Config.WIDTH_GAME, Config.HEIGHT_GAME)
    private val stage = Stage(ScreenViewport(camera))

    init {
        loadResources()
        Gdx.input.inputProcessor = stage
    }

    private fun loadResources(){
        manager.load(Descriptors.background)
        manager.load(Descriptors.menu)
        manager.finishLoading()
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

        stage.addActor(background)
        stage.addActor(title)
        title.animate()
        stage.addActor(restartIcon)
        restartIcon.animate()
        stage.addActor(mainMenuIcon)
        mainMenuIcon.animate()
        stage.addActor(shadow)
        shadow.animate()
        AudioManager.play(MAIN_MENU_MUSIC)

        addClickListener(restartIcon) {
            AudioManager.stopAll()
            title.animate(true)
            restartIcon.animate(true)
            mainMenuIcon.animate(true)
            shadow.animate(true, Runnable {
                ScreenManager.setScreen(GAME_SCREEN)
            })
        }

        addClickListener(mainMenuIcon) {
            title.animate(true)
            restartIcon.animate(true)
            mainMenuIcon.animate(true)
            shadow.animate(true, Runnable {
                ScreenManager.setScreen(START_SCREEN)
            })
        }
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