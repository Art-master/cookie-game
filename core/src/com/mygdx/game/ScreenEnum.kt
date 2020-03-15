package com.mygdx.game

import com.badlogic.gdx.Screen
import com.mygdx.game.screens.GameScreen
import com.mygdx.game.screens.StartScreen

enum class ScreenEnum(val screen: Screen) {
    MAIN_MENU(StartScreen()),
    GAME(GameScreen()),
    GAME_OVER(GameScreen())
}