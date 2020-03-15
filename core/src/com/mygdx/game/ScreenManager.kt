package com.mygdx.game

import com.badlogic.gdx.Game

object ScreenManager {
    var game: Game? = null

    fun setScreen(screenEnum: ScreenEnum = ScreenEnum.MAIN_MENU, vararg params: Any = emptyArray()) {
        val currentScreen = game?.screen
        val newScreen = screenEnum.screen
        game?.screen = newScreen
        currentScreen?.dispose()
    }
}