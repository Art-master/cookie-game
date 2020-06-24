package com.mygdx.game

import com.badlogic.gdx.Game
import com.mygdx.game.ads.AdsController
import com.mygdx.game.api.GameSettings
import com.mygdx.game.managers.ScreenManager
import com.mygdx.game.managers.ScreenManager.Params.ADS_CONTROLLER
import com.mygdx.game.managers.ScreenManager.Screens.*

class GdxGame(private val adsController: AdsController) : Game() {

    override fun create() {
        ScreenManager.game = this
        ScreenManager.setScreen(START_SCREEN, Pair(ADS_CONTROLLER, adsController))
        GameSettings.DEBUG.state = false
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {}
}
