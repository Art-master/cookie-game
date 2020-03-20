package com.mygdx.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import com.mygdx.game.screens.StartScreen


object ScreenManager {
    var game: Game? = null

    fun setScreen(screen: Screen = StartScreen(), vararg params: Any = emptyArray()) {
        val currentScreen = game?.screen
        game?.screen = screen
        currentScreen?.dispose()
    }
}