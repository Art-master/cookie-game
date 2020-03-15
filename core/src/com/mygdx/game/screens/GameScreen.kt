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
    }

    override fun show() {
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
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        gameWorld?.stage?.dispose()
    }
}