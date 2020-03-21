package com.mygdx.game

import com.badlogic.gdx.Game
import com.mygdx.game.screens.GameOverScreen
import com.mygdx.game.screens.GameScreen
import com.mygdx.game.screens.StartScreen


object ScreenManager {
    var game: Game? = null

    enum class Screens{
        START_SCREEN, GAME_SCREEN, GAME_OVER
    }

    fun setScreen(screen: Screens = Screens.START_SCREEN, vararg params: Any = emptyArray()) {
        val currentScreen = game?.screen
        val nextScreen= when(screen){
            Screens.START_SCREEN -> StartScreen()
            Screens.GAME_SCREEN -> GameScreen()
            Screens.GAME_OVER -> GameOverScreen()
        }
        game?.screen = nextScreen
        currentScreen?.dispose()
    }
}