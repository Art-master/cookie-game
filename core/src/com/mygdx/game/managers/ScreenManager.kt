package com.mygdx.game.managers

import com.badlogic.gdx.Game
import com.mygdx.game.screens.*
import java.util.*

object ScreenManager {
    var game: Game? = null
    val globalParameters = EnumMap<Param, Any>(Param::class.java)

    enum class Screens{
        LOADING_SCREEN, START_SCREEN, GAME_SCREEN, GAME_OVER, COMICS_SCREEN
    }

    enum class Param{
        SCORE, ADS_CONTROLLER, ASSET_MANAGER, FIRST_APP_RUN
    }

    fun setScreen(screen: Screens = Screens.START_SCREEN, params: Map<Param, Any> = EnumMap(Param::class.java)) {
        val allParams = params.plus(globalParameters)
        val currentScreen = game?.screen
        val nextScreen= when(screen){
            Screens.LOADING_SCREEN -> LoadingScreen(allParams)
            Screens.START_SCREEN -> StartScreen(allParams)
            Screens.GAME_SCREEN -> GameScreen(allParams)
            Screens.GAME_OVER -> GameOverScreen(allParams)
            Screens.COMICS_SCREEN -> ComicsScreen(allParams)
        }
        game?.screen = nextScreen
        currentScreen?.dispose()
    }

    fun setScreen(screen: Screens = Screens.START_SCREEN, vararg params: Pair<Param, Any>){
        setScreen(screen, params.toMap())
    }

    fun setGlobalParameter(param: Param, value: Any){
        globalParameters[param] = value
    }
}