package com.mygdx.game

import com.badlogic.gdx.Game
import com.mygdx.game.managers.ScreenManager
import com.mygdx.game.managers.ScreenManager.Param.SERVICES_CONTROLLER
import com.mygdx.game.managers.ScreenManager.Screens.*
import com.mygdx.game.services.ServicesController

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
