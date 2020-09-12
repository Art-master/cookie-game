package com.mygdx.game

import com.badlogic.gdx.Game
import com.mygdx.game.services.AdsController
import com.mygdx.game.api.GameSettings
import com.mygdx.game.managers.ScreenManager
import com.mygdx.game.managers.ScreenManager.Param.SERVICES_CONTROLLER
import com.mygdx.game.managers.ScreenManager.Screens.*
import com.mygdx.game.services.AchievementsController
import com.mygdx.game.services.ServicesController
import com.mygdx.game.services.LeaderboardController
import java.lang.Error

class GdxGame(private val controller: ServicesController) : Game() {

    override fun create() {
        when (controller) {
            !is AchievementsController -> {
                throw Error("Launch class must be implemented from AchievementsController")
            }
            !is LeaderboardController -> {
                throw Error("Launch class must be implemented from LeaderboardController")
            }
            !is AdsController -> {
                throw Error("Launch class must be implemented fromAdsController")
            }
            else -> {
                ScreenManager.game = this
                ScreenManager.globalParameters[SERVICES_CONTROLLER] = controller
                ScreenManager.setScreen(LOADING_SCREEN)
                GameSettings.DEBUG.state = Config.DEBUG_COLLISIONS
                controller.signIn()
            }
        }
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {}
}
