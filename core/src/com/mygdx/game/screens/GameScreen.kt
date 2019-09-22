package com.mygdx.game.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.mygdx.game.data.Descriptors
import com.mygdx.game.world.GameWorld

class GameScreen : Screen {
    private var runTime = 0f
    private val manager = AssetManager()
    private var gameWorld : GameWorld? = null
    init {
        loadResources()
    }

    private fun loadResources(){
        manager.load(Descriptors.background)
        manager.load(Descriptors.cookie)
        manager.load(Descriptors.environment)
        manager.finishLoading()
    }

    override fun hide() {
        Gdx.app.log("GameScreen", "hide called")
    }

    override fun show() {
        Gdx.app.log("GameScreen", "show called")
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if(manager.isFinished && gameWorld == null){
            gameWorld = GameWorld(manager)
        }

        gameWorld!!.update(delta)
        gameWorld!!.stage.draw()
        runTime += delta
    }

    override fun pause() {
        Gdx.app.log("GameScreen", "pause called")
    }

    override fun resume() {
        Gdx.app.log("GameScreen", "resume called")
    }

    override fun resize(width: Int, height: Int) {
        Gdx.app.log("GameScreen", "resize called")
    }

    override fun dispose() {
        Gdx.app.log("GameScreen", "dispose called")

    }
}