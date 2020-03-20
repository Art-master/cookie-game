package com.mygdx.game

import com.badlogic.gdx.Game
import com.mygdx.game.screens.GameOverScreen

class GdxGame : Game() {

    override fun create() {
        ScreenManager.game = this
        ScreenManager.setScreen(GameOverScreen())
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {}
}
