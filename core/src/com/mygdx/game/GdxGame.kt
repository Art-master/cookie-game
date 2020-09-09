package com.mygdx.game

import com.badlogic.gdx.Game
import com.mygdx.game.services.AdsController
import com.mygdx.game.api.GameSettings
import com.mygdx.game.managers.ScreenManager
import com.mygdx.game.managers.ScreenManager.Param.SERVICES_CONTROLLER
import com.mygdx.game.managers.ScreenManager.Screens.*
import com.mygdx.game.services.ServicesController
import com.mygdx.game.services.LeaderboardController

class GdxGame(private val controller: ServicesController) : Game() {

    override fun create() {
        ScreenManager.game = this
        ScreenManager.globalParameters[SERVICES_CONTROLLER] = controller
        ScreenManager.setScreen(LOADING_SCREEN)
        GameSettings.DEBUG.state = false
        if (controller is LeaderboardController) controller.signIn()
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {}
}
