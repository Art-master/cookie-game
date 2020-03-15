package com.mygdx.game

import com.badlogic.gdx.Game

class GdxGame : Game() {

    override fun create() {
        ScreenManager.game = this
        ScreenManager.setScreen()
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {}
}
