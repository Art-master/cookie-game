package com.mygdx.game.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.Gdx
import com.mygdx.game.ScreenConfig
import com.mygdx.game.render.GameRender

class GameScreen : Screen {
    private val renderer : GameRender
    private var runTime = 0f

    init {
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        val gameWidth = ScreenConfig.widthGame
        val gameHeight = screenHeight / (screenWidth / gameWidth)
        renderer = GameRender()
    }

    override fun hide() {
        Gdx.app.log("GameScreen", "hide called")
    }

    override fun show() {
        Gdx.app.log("GameScreen", "show called")
    }

    override fun render(delta: Float) {
        runTime += delta
        renderer.render(delta, runTime)
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