package com.run.cookie.run.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Timer
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.Prefs
import com.run.cookie.run.game.actors.loading_progress.Background
import com.run.cookie.run.game.actors.loading_progress.ProgressBar
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import com.run.cookie.run.game.managers.AudioManager
import com.run.cookie.run.game.managers.ScreenManager
import com.run.cookie.run.game.managers.ScreenManager.Param.*
import com.run.cookie.run.game.managers.ScreenManager.Screens.MAIN_MENU_SCREEN
import com.run.cookie.run.game.services.AdsController


class LoadingScreen(params: Map<ScreenManager.Param, Any>) : Screen {

    private val manager = AssetManager()
    private val adsManager = params[SERVICES_CONTROLLER] as AdsController
    private val camera = OrthographicCamera(Config.WIDTH_GAME, Config.HEIGHT_GAME)
    private val stage = Stage(ExtendViewport(Config.WIDTH_GAME, Config.HEIGHT_GAME, camera))

    private var progressBar: ProgressBar? = null

    private var firstRun = false

    init {
        val prefs = Gdx.app.getPreferences(Prefs.NAME)
        firstRun = prefs.getBoolean(Prefs.FIRST_RUN, true)
        if (firstRun) prefs.putBoolean(Prefs.FIRST_RUN, false).flush()
        ScreenManager.setGlobalParameter(FIRST_APP_RUN, firstRun)

        manager.load(Descriptors.progressBar)
        manager.finishLoadingAsset<AssetDescriptor<TextureAtlas>>(Descriptors.progressBar)
        if (firstRun.not()) adsManager.showBannerAd()

        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if (stage.actors.isEmpty && manager.isLoaded(Descriptors.progressBar)) {
            initProgressBarActors()
            loadResources()
        }

        if (manager.update()) {
            progressBar?.setProgress(0.100f)

            Timer.schedule(object : Timer.Task() {
                override fun run() {
                    adsManager.hideBannerAd()
                    setTexturesFilters(manager.get(Descriptors.background))
                    setTexturesFilters(manager.get(Descriptors.gameOverBackground))
                    setTexturesFilters(manager.get(Descriptors.comics))
                    setTexturesFilters(manager.get(Descriptors.menu))
                    setTexturesFilters(manager.get(Descriptors.cookie))
                    setTexturesFilters(manager.get(Descriptors.environment))

                    ScreenManager.setGlobalParameter(ASSET_MANAGER, manager)
                    ScreenManager.setScreen(MAIN_MENU_SCREEN)
                }
            }, 0.2f)

        } else progressBar?.setProgress(manager.progress)

        stage.act(delta)
        stage.draw()
    }

    private fun setTexturesFilters(data: Disposable) {
        if (data is Texture) {
            data.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        } else if (data is TextureAtlas) {
            data.textures.forEach { it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) }
        }
    }

    private fun initProgressBarActors() {
        val background = Background(manager)
        progressBar = ProgressBar(manager)

        stage.apply {
            addActor(background)
            addActor(progressBar)
        }
    }

    private fun loadResources() {
        manager.load(Descriptors.progressBar) // store unfiltered regions
        manager.load(Descriptors.background)
        manager.load(Descriptors.gameOverBackground)
        manager.load(Descriptors.comics)
        manager.load(Descriptors.menu)
        manager.load(Descriptors.cookie)
        manager.load(Descriptors.environment)
        loadFonts()
        AudioManager.loadMusic(manager)
        AudioManager.loadSounds(manager)
    }

    private fun loadFonts() {
        val resolver: FileHandleResolver = InternalFileHandleResolver()
        manager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        manager.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))
        manager.load(Descriptors.scoreFont)
        manager.load(Descriptors.currentScoreFont)
        manager.load(Descriptors.bestScoreFont)
    }


    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        manager.unload(Assets.ProgressAtlas.NAME)
        stage.dispose()
    }
}