package com.run.cookie.run.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.actors.Shadow
import com.run.cookie.run.game.managers.ScreenManager
import com.run.cookie.run.game.services.AdsController

abstract class GameScreen(params: Map<ScreenManager.Param, Any>) : Screen {
    val manager = params[ScreenManager.Param.ASSET_MANAGER] as AssetManager

    val adsController = params[ScreenManager.Param.SERVICES_CONTROLLER] as AdsController

    val camera = OrthographicCamera(Config.WIDTH_GAME, Config.HEIGHT_GAME)
    val stageBackground = Stage(FillViewport(Config.WIDTH_GAME, Config.HEIGHT_GAME, camera))
    var stage = Stage(FitViewport(Config.WIDTH_GAME, Config.HEIGHT_GAME, camera))
    private val stageShadow = Stage(FillViewport(Config.WIDTH_GAME, Config.HEIGHT_GAME, camera))
    val shadow = Shadow(manager)

    private var profiler: GLProfiler? = null
    private var fpsLogger: FPSLogger? = null

    init {
        stageShadow.addActor(shadow)

        if (Config.Debug.PROFILER.state) {
            profiler = GLProfiler(Gdx.graphics)
            profiler!!.enable()
        }

        if (Config.Debug.FPS.state) fpsLogger = FPSLogger()
    }

    fun applyStages(delta: Float) {
        stageBackground.viewport.apply()
        stageBackground.act(delta)
        stageBackground.draw()

        stage.viewport.apply()
        stage.act(delta)
        stage.draw()

        stageShadow.viewport.apply()
        stageShadow.act(delta)
        stageShadow.draw()

        profiler?.let {
            val drawCalls = profiler!!.drawCalls.toFloat()
            val textureBinds = profiler!!.textureBindings.toFloat()
            println("===== render ======")
            println("draw calls = $drawCalls")
            println("texture binds = $textureBinds")
            profiler!!.reset()
        }

        fpsLogger?.log()
    }

    override fun dispose() {
        stageBackground.dispose()
        stage.dispose()
        //stageShadow.dispose() // Early disposing lead to blink
    }
}