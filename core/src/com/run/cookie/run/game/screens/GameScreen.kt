package com.run.cookie.run.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.managers.AudioManager
import com.run.cookie.run.game.managers.AudioManager.MusicApp
import com.run.cookie.run.game.managers.ScreenManager
import com.run.cookie.run.game.world.GameWorld

class GameScreen(params: Map<ScreenManager.Param, Any>) : Screen {
    private var runTime = 0f
    private var manager = params[ScreenManager.Param.ASSET_MANAGER] as AssetManager
    private var gameWorld : GameWorld? = null

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val bufferBitMv = if (Gdx.graphics.bufferFormat.coverageSampling) GL20.GL_COVERAGE_BUFFER_BIT_NV else 0
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT or bufferBitMv)

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
        Config.currentScrollSpeed = Config.DEFAULT_SCROLL_SPEED
        gameWorld?.stage?.dispose()
    }
}