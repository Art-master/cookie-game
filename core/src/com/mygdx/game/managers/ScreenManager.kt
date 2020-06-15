package com.mygdx.game.managers

import com.badlogic.gdx.Game
import com.mygdx.game.screens.ComicsScreen
import com.mygdx.game.screens.GameOverScreen
import com.mygdx.game.screens.GameScreen
import com.mygdx.game.screens.StartScreen


object ScreenManager {
    var game: Game? = null

    enum class Screens{
        START_SCREEN, GAME_SCREEN, GAME_OVER, COMICS_SCREEN
    }

    fun setScreen(screen: Screens = Screens.START_SCREEN, vararg params: Any = emptyArray()) {
        val currentScreen = game?.screen
        val nextScreen= when(screen){
            Screens.START_SCREEN -> StartScreen()
            Screens.GAME_SCREEN -> GameScreen(params)
            Screens.GAME_OVER -> GameOverScreen(params)
            Screens.COMICS_SCREEN -> ComicsScreen(params)
        }
        game?.screen = nextScreen
        currentScreen?.dispose()
    }
}