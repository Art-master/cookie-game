/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

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
import com.badlogic.gdx.utils.Timer
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.Prefs
import com.run.cookie.run.game.actors.loading_progress.Background
import com.run.cookie.run.game.actors.loading_progress.ProgressBar
import com.run.cookie.run.game.api.Advertising
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import com.run.cookie.run.game.managers.AudioManager
import com.run.cookie.run.game.managers.ScreenManager
import com.run.cookie.run.game.managers.ScreenManager.Param.*
import com.run.cookie.run.game.managers.ScreenManager.Screens.MAIN_MENU_SCREEN


class LoadingScreen(params: Map<ScreenManager.Param, Any>) : Screen {

    private val manager = AssetManager()
    private val camera = OrthographicCamera(Config.WIDTH_GAME, Config.HEIGHT_GAME)
    private val bgStage = Stage(FillViewport(Config.WIDTH_GAME, Config.HEIGHT_GAME, camera))
    private var stage = Stage(FitViewport(Config.WIDTH_GAME, Config.HEIGHT_GAME, camera))

    private var progressBar: ProgressBar? = null

    private var firstRun = false

    init {
        val prefs = Gdx.app.getPreferences(Prefs.NAME)
        firstRun = prefs.getBoolean(Prefs.FIRST_RUN, true)
        if (firstRun) prefs.putBoolean(Prefs.FIRST_RUN, false).flush()
        ScreenManager.setGlobalParameter(FIRST_APP_RUN, firstRun)
        ScreenManager.setGlobalParameter(AD, Advertising())

        loadStartResources()
        initProgressBarActors()
        loadResources()

        Gdx.input.inputProcessor = stage
        Texture.setAssetManager(manager)
    }

    private fun loadStartResources() {
        manager.load(Descriptors.progressBar)
        manager.finishLoadingAsset<AssetDescriptor<TextureAtlas>>(Descriptors.progressBar)
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if (progressBar?.progress != 100) {
            if (manager.update()) {
                progressBar?.progress = 100

                Timer.schedule(object : Timer.Task() {
                    override fun run() {
                        loadResourcesFinished()
                    }
                }, 0.2f)

            } else progressBar?.progress = (manager.progress * 100).toInt()
        }

        bgStage.viewport.apply()
        bgStage.act(delta)
        bgStage.draw()

        stage.viewport.apply()
        stage.act(delta)
        stage.draw()
    }

    private fun loadResourcesFinished() {
        AudioManager.onMusicsLoaded(manager)
        AudioManager.onSoundsLoaded(manager)
        ScreenManager.setGlobalParameter(ASSET_MANAGER, manager)
        ScreenManager.setScreen(MAIN_MENU_SCREEN)
    }

    private fun initProgressBarActors() {
        val background = Background(manager)
        progressBar = ProgressBar(manager)

        bgStage.addActor(background)
        stage.addActor(progressBar)
    }

    private fun loadResources() {
        AudioManager.loadMusic(manager)
        AudioManager.loadSounds(manager)

        manager.load(Descriptors.progressBar)
        manager.load(Descriptors.background)
        manager.load(Descriptors.gameOverBackground)
        manager.load(Descriptors.comics)
        manager.load(Descriptors.menu)
        manager.load(Descriptors.cookie)
        manager.load(Descriptors.environment)
        loadFonts()
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
        bgStage.dispose()
    }
}