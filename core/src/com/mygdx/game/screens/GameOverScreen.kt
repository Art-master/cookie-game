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
import com.mygdx.game.actors.game_over_screen.GameOverTitle
import com.mygdx.game.actors.game_over_screen.MainMenuIcon
import com.mygdx.game.actors.game_over_screen.RestartIcon
import com.mygdx.game.actors.main_menu_screen.*
import com.mygdx.game.data.Descriptors

class GameOverScreen : Screen {
    private val manager = AssetManager()
    private val camera = OrthographicCamera(Config.widthGame, Config.heightGame)
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

        stage.addActor(background)
        stage.addActor(title)
        stage.addActor(restartIcon)
        stage.addActor(mainMenuIcon)
        addClickListener(restartIcon) {
            title.move()
        }
    }

    private fun addClickListener(actor: Actor, function: () -> Unit){
        actor.addListener(object: ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
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