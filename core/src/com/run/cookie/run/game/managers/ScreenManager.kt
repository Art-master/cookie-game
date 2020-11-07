package com.run.cookie.run.game.managers

import com.badlogic.gdx.Game
import com.run.cookie.run.game.screens.*
import java.util.*

object ScreenManager {
    var game: Game? = null
    val globalParameters = EnumMap<Param, Any>(Param::class.java)

    enum class Screens{
        LOADING_SCREEN, MAIN_MENU_SCREEN, GAME_SCREEN, GAME_OVER, COMICS_SCREEN
    }

    enum class Param{
        SCORE, SERVICES_CONTROLLER, ASSET_MANAGER, FIRST_APP_RUN, WAS_WIN_GAME, SCREEN_LINK
    }

    fun setScreen(screen: Screens = Screens.MAIN_MENU_SCREEN, params: Map<Param, Any> = EnumMap(Param::class.java)) {
        val allParams = params.plus(globalParameters)
        val currentScreen = game?.screen
        val nextScreen= when(screen){
            Screens.LOADING_SCREEN -> LoadingScreen(allParams)
            Screens.MAIN_MENU_SCREEN -> MainMenuScreen(allParams)
            Screens.GAME_SCREEN -> GamePlayScreen(allParams)
            Screens.GAME_OVER -> GameOverScreen(allParams)
            Screens.COMICS_SCREEN -> ComicsScreen(allParams)
        }
        game?.screen = nextScreen
        currentScreen?.dispose()
    }

    fun setScreen(screen: Screens = Screens.MAIN_MENU_SCREEN, vararg params: Pair<Param, Any>){
        setScreen(screen, params.toMap())
    }

    fun setGlobalParameter(param: Param, value: Any){
        globalParameters[param] = value
    }
}