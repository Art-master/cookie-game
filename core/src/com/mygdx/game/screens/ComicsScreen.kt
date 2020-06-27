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
import com.mygdx.game.actors.comics.*
import com.mygdx.game.api.AnimationType
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.ScreenManager
import com.mygdx.game.managers.ScreenManager.Param
import com.mygdx.game.managers.ScreenManager.Param.ASSET_MANAGER
import com.mygdx.game.managers.ScreenManager.Screens.*

class ComicsScreen(params: Map<Param, Any>) : Screen {

    private var manager = params[ASSET_MANAGER] as AssetManager
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
        val frame1 = ComicsFrame1(manager, background)
        val frame2 = ComicsFrame2(manager, frame1)
        val frame3 = ComicsFrame3(manager, frame1)
        val frame4 = ComicsFrame4(manager, frame2, frame3)
        val arrowForward = ArrowForward(manager)
        val shadow = Shadow(manager)

        stage.apply {
            addActor(background)
            addActor(frame1)
            addActor(frame2)
            addActor(frame3)
            addActor(frame4)
            addActor(arrowForward)
            addActor(shadow)
        }

        shadow.animate(AnimationType.SHOW_ON_SCENE)
        frame1.animate(AnimationType.SHOW_ON_SCENE,
                Runnable {
                    frame2.animate(AnimationType.SHOW_ON_SCENE,
                            Runnable {
                                frame3.animate(AnimationType.SHOW_ON_SCENE,
                                        Runnable {
                                            frame4.animate(AnimationType.SHOW_ON_SCENE,
                                                    Runnable {
                                                        arrowForward.animate(AnimationType.SHOW_ON_SCENE)
                                                    })
                                        })
                            })
                })

        AudioManager.play(AudioManager.MusicApp.MAIN_MENU_MUSIC)

        addClickListener(background) {
            AudioManager.stopAll()
            shadow.animate(AnimationType.HIDE_FROM_SCENE, Runnable {
                ScreenManager.setScreen(GAME_SCREEN)
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