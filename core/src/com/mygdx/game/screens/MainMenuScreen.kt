package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.Config
import com.mygdx.game.actors.Shadow
import com.mygdx.game.actors.main_menu_screen.*
import com.mygdx.game.services.AdsController
import com.mygdx.game.api.AnimationType
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.AudioManager.MusicApp
import com.mygdx.game.managers.AudioManager.SoundApp
import com.mygdx.game.managers.ScreenManager
import com.mygdx.game.managers.ScreenManager.Param.*
import com.mygdx.game.managers.ScreenManager.Screens.COMICS_SCREEN
import com.mygdx.game.managers.ScreenManager.Screens.GAME_SCREEN


class MainMenuScreen(params: Map<ScreenManager.Param, Any>) : Screen {

    private var manager = params[ASSET_MANAGER] as AssetManager
    private var firstAppRun = params[FIRST_APP_RUN] as Boolean

    private var adsController = params[SERVICES_CONTROLLER] as AdsController

    private val camera = OrthographicCamera(Config.WIDTH_GAME, Config.HEIGHT_GAME)
    private val stage = Stage(ScreenViewport(camera))

    init {
        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if (manager.isFinished && stage.actors.isEmpty) {
            addActorsToStage()
        }
        stage.act(delta)
        stage.draw()
    }

    private fun addActorsToStage() {
        val background = Background(manager)
        val title = MainTitle(manager)
        val soundIcon = MusicIcon(manager)
        val musicIcon = SoundIcon(manager, soundIcon)
        val vibrationIcon = VibrationIcon(manager, soundIcon)
        val playButton = PlayButton(manager)
        val shadow = Shadow(manager)

        stage.apply {
            addActor(background)
            addActor(title)
            addActor(soundIcon)
            addActor(musicIcon)
            addActor(vibrationIcon)
            addActor(playButton)
            addActor(shadow)
        }

        title.animate(AnimationType.SHOW_ON_SCENE)
        soundIcon.animate(AnimationType.SHOW_ON_SCENE)
        musicIcon.animate(AnimationType.SHOW_ON_SCENE)
        vibrationIcon.animate(AnimationType.SHOW_ON_SCENE)
        playButton.animate(AnimationType.SHOW_ON_SCENE)
        shadow.animate(AnimationType.SHOW_ON_SCENE)

        addClickListener(playButton) {
            playButton.animate(AnimationType.HIDE_FROM_SCENE)
            title.animate(AnimationType.HIDE_FROM_SCENE)
            soundIcon.animate(AnimationType.HIDE_FROM_SCENE)
            musicIcon.animate(AnimationType.HIDE_FROM_SCENE)
            vibrationIcon.animate(AnimationType.HIDE_FROM_SCENE)
            shadow.animate(AnimationType.HIDE_FROM_SCENE, Runnable {
                setScreen()
                AudioManager.stopAll()
            })
        }
        AudioManager.play(MusicApp.MAIN_MENU_MUSIC, true)
    }

    private fun setScreen() {
        //firstRun = true // TODO test
        if (firstAppRun) ScreenManager.setScreen(COMICS_SCREEN)
        else ScreenManager.setScreen(GAME_SCREEN)
    }

    private fun addClickListener(actor: Actor, function: () -> Unit) {
        actor.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                function()
                AudioManager.play(SoundApp.CRUNCH)
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        stage.dispose()
    }
}