package com.mygdx.game.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.AudioManager.MusicApp
import com.mygdx.game.world.GameWorld

class GameScreen(params: Array<out Any>) : Screen {
    private var runTime = 0f
    private val manager: AssetManager = params.first { it is AssetManager } as AssetManager
    private var gameWorld : GameWorld? = null

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if(manager.isFinished && gameWorld == null){
            gameWorld = GameWorld(manager)
            AudioManager.play(MusicApp.GAME_MUSIC, true)
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