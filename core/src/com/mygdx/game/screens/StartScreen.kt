package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.Config
import com.mygdx.game.managers.ScreenManager
import com.mygdx.game.managers.ScreenManager.Screens.*
import com.mygdx.game.actors.Shadow
import com.mygdx.game.actors.main_menu_screen.*
import com.mygdx.game.data.Descriptors
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.AudioManager.MusicApp
import com.mygdx.game.managers.AudioManager.Sounds
import com.mygdx.game.managers.VibrationManager

class StartScreen : Screen {
    private val manager = AssetManager()
    private val camera = OrthographicCamera(Config.WIDTH_GAME, Config.HEIGHT_GAME)
    private val stage = Stage(ScreenViewport(camera))

    init {
        loadResources()
        Gdx.input.inputProcessor = stage
    }

    private fun loadResources(){
        manager.load(Descriptors.background)
        manager.load(Descriptors.menu)
        manager.load(Descriptors.environment)
        manager.finishLoading()
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if(manager.isFinished && stage.actors.isEmpty){
            addActorsToStage()
        }
        stage.act(delta)
        stage.draw()
    }

    private fun addActorsToStage(){
        val background = Background(manager)
        val title = MainTitle(manager)
        val soundIcon = SoundIcon(manager)
        val vibrationIcon = VibrationIcon(manager, soundIcon)
        val playButton = PlayButton(manager)
        val shadow = Shadow(manager)

        stage.addActor(background)
        stage.addActor(title)
        title.animate()
        stage.addActor(soundIcon)
        soundIcon.animate()
        stage.addActor(vibrationIcon)
        vibrationIcon.animate()
        stage.addActor(playButton)
        playButton.animate()
        stage.addActor(shadow)
        shadow.animate()

        addClickListener(playButton) {
            playButton.animate(true)
            title.animate(true)
            soundIcon.animate(true)
            vibrationIcon.animate(true)
            shadow.animate(true, Runnable {
                ScreenManager.setScreen(GAME_SCREEN)
                AudioManager.stopAll()
            })
        }
        AudioManager.play(MusicApp.MAIN_MENU_MUSIC, true)
    }

    private fun addClickListener(actor: Actor, function: () -> Unit){
        actor.addListener(object: ClickListener(){
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                function()
                AudioManager.play(Sounds.CRUNCH)
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