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
import com.mygdx.game.actors.Comics
import com.mygdx.game.actors.Shadow
import com.mygdx.game.api.AnimationType
import com.mygdx.game.data.Descriptors
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.ScreenManager

class ComicsScreen(params: Array<out Any>) : Screen {

    private var manager: AssetManager
    private val camera = OrthographicCamera(Config.WIDTH_GAME, Config.HEIGHT_GAME)
    private val stage = Stage(ScreenViewport(camera))

    init {
        Gdx.input.inputProcessor = stage
        manager = params.first { it is AssetManager } as AssetManager

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
        val comics = Comics(manager)
        val shadow = Shadow(manager)

        stage.apply {
            addActor(comics)
            addActor(shadow)
        }

        shadow.animate(AnimationType.SHOW_ON_SCENE)
        AudioManager.play(AudioManager.MusicApp.MAIN_MENU_MUSIC)

        addClickListener(comics) {
            AudioManager.stopAll()
            shadow.animate(AnimationType.HIDE_FROM_SCENE, Runnable {
                ScreenManager.setScreen(ScreenManager.Screens.GAME_SCREEN)
            })
        }
    }

    private fun addClickListener(actor: Actor, function: () -> Unit) {
        actor.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                AudioManager.play(AudioManager.Sound.CLICK_SOUND)
                function()
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