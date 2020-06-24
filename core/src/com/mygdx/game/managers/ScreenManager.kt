package com.mygdx.game.managers

import com.badlogic.gdx.Game
import com.mygdx.game.screens.ComicsScreen
import com.mygdx.game.screens.GameOverScreen
import com.mygdx.game.screens.GameScreen
import com.mygdx.game.screens.StartScreen
import java.util.*
import kotlin.collections.HashMap


object ScreenManager {
    var game: Game? = null
    val parameters = HashMap<String, Any>()

    enum class Screens{
        START_SCREEN, GAME_SCREEN, GAME_OVER, COMICS_SCREEN
    }

    enum class Params{
        SCORE, ADS_CONTROLLER, ASSET_MANAGER
    }

    fun setScreen(screen: Screens = Screens.START_SCREEN, params: Map<Params, Any> = EnumMap(Params::class.java)) {
        val currentScreen = game?.screen
        val nextScreen= when(screen){
            Screens.START_SCREEN -> StartScreen(params)
            Screens.GAME_SCREEN -> GameScreen(params)
            Screens.GAME_OVER -> GameOverScreen(params)
            Screens.COMICS_SCREEN -> ComicsScreen(params)
        }
        game?.screen = nextScreen
        currentScreen?.dispose()
    }

    fun setScreen(screen: Screens = Screens.START_SCREEN, vararg params: Pair<Params, Any>){
        setScreen(screen, params.toMap())
    }
}