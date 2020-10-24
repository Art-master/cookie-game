package com.run.cookie.run.game

import com.badlogic.gdx.Game
import com.run.cookie.run.game.managers.ScreenManager
import com.run.cookie.run.game.managers.ScreenManager.Param.SERVICES_CONTROLLER
import com.run.cookie.run.game.managers.ScreenManager.Screens.*
import com.run.cookie.run.game.services.ServicesController

class GdxGame(private val controller: ServicesController) : Game() {

    override fun create() {
        ScreenManager.game = this
        ScreenManager.globalParameters[SERVICES_CONTROLLER] = controller
        ScreenManager.setScreen(LOADING_SCREEN)
        controller.signIn()
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {}
}
