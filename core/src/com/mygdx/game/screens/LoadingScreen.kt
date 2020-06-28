package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Timer
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.Config
import com.mygdx.game.Prefs
import com.mygdx.game.actors.loading_progress.Background
import com.mygdx.game.actors.loading_progress.ProgressBar
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.ScreenManager
import com.mygdx.game.managers.ScreenManager.Param.ASSET_MANAGER
import com.mygdx.game.managers.ScreenManager.Param.FIRST_APP_RUN
import com.mygdx.game.managers.ScreenManager.Screens.START_SCREEN

class LoadingScreen(params: Map<ScreenManager.Param, Any>) : Screen {

    private val manager = AssetManager()

    private val camera = OrthographicCamera(Config.WIDTH_GAME, Config.HEIGHT_GAME)
    private val stage = Stage(ScreenViewport(camera))

    var progressBar: ProgressBar? = null

    var firstRun = false

    init {
        val prefs = Gdx.app.getPreferences(Prefs.NAME)
        firstRun = prefs.getBoolean(Prefs.FIRST_RUN, true)
        if(firstRun) prefs.putBoolean(Prefs.FIRST_RUN, false)
        ScreenManager.setGlobalParameter(FIRST_APP_RUN, firstRun)

        manager.load(Descriptors.progressBar)
        manager.finishLoadingAsset(Descriptors.progressBar)

        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if(stage.actors.isEmpty && manager.isLoaded(Descriptors.progressBar)){
            initProgressBarActors()
            loadResources()
        }

        if(manager.update()){
            progressBar?.setProgress(0.100f)

            Timer.schedule(object : Timer.Task() {
                override fun run() {
                    ScreenManager.setGlobalParameter(ASSET_MANAGER, manager)
                    ScreenManager.setScreen(START_SCREEN)
                }
            }, 0.2f)

        } else progressBar?.setProgress(manager.progress)

        stage.act(delta)
        stage.draw()
    }

    private fun initProgressBarActors(){
        val background = Background(manager)
        progressBar = ProgressBar(manager)

        stage.apply {
            addActor(background)
            addActor(progressBar)
        }
    }

    private fun loadResources(){
        manager.load(Descriptors.background)
        manager.load(Descriptors.gameOverBackground)
        if(firstRun) manager.load(Descriptors.comics)
        manager.load(Descriptors.menu)
        manager.load(Descriptors.cookie)
        manager.load(Descriptors.environment)
        loadFonts()
        AudioManager.loadMusic(manager)
        AudioManager.loadSounds(manager)
    }

    private fun loadFonts(){
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