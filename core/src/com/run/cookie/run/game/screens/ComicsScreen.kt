package com.run.cookie.run.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.run.cookie.run.game.actors.comics.*
import com.run.cookie.run.game.api.AnimationType
import com.run.cookie.run.game.managers.AudioManager
import com.run.cookie.run.game.managers.ScreenManager
import com.run.cookie.run.game.managers.ScreenManager.Param
import com.run.cookie.run.game.managers.ScreenManager.Screens.GAME_SCREEN
import com.run.cookie.run.game.managers.VibrationManager

class ComicsScreen(private val params: Map<Param, Any>) : GameScreen(params) {

    private var isMainButtonHost = params[Param.SCREEN_LINK] == null

    init {
        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if (stage.actors.isEmpty) addActorsToStage()
        applyStages(delta)
    }

    private fun addActorsToStage() {
        val background = Background(manager)
        val frame1 = ComicsFrame1(manager, background)
        val frame2 = ComicsFrame2(manager, frame1)
        val frame3 = ComicsFrame3(manager, frame1)
        val frame4 = ComicsFrame4(manager, frame2, frame3)
        val arrowForward = ArrowForward(manager)

        stageBackground.addActor(background)
        stage.apply {
            addActor(frame1)
            addActor(frame2)
            addActor(frame3)
            addActor(frame4)
            addActor(arrowForward)
        }

        shadow.animate(AnimationType.SHOW_ON_SCENE)
        if (isMainButtonHost)
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
        if (isMainButtonHost.not()) {
            frame1.setToFinalPosition()
            frame2.setToFinalPosition()
            frame3.setToFinalPosition()
            frame4.setToFinalPosition()
            arrowForward.animate(AnimationType.SHOW_ON_SCENE)
        }

        AudioManager.play(AudioManager.MusicApp.MAIN_MENU_MUSIC)

        addClickListener(stage) {
            AudioManager.stopAll()
            VibrationManager.vibrate()
            shadow.animate(AnimationType.HIDE_FROM_SCENE, Runnable {
                if (isMainButtonHost) {
                    ScreenManager.setScreen(GAME_SCREEN)
                } else {
                    val screen = params[Param.SCREEN_LINK] as ScreenManager.Screens
                    ScreenManager.setScreen(screen)
                }
            })
        }
    }

    private fun <T : Stage> addClickListener(obj: T, function: () -> Unit) {
        obj.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                AudioManager.play(AudioManager.SoundApp.CLICK_SOUND)
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
        super.dispose()
    }
}