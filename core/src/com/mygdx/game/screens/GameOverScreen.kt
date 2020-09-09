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
import com.mygdx.game.managers.ScreenManager
import com.mygdx.game.managers.ScreenManager.Screens.*
import com.mygdx.game.actors.Shadow
import com.mygdx.game.actors.game_over_screen.*
import com.mygdx.game.services.AdsController
import com.mygdx.game.api.AnimationType.*
import com.mygdx.game.managers.AudioManager
import com.mygdx.game.managers.AudioManager.MusicApp.*
import com.mygdx.game.managers.AudioManager.SoundApp.*
import com.mygdx.game.managers.ScreenManager.Param.*
import com.mygdx.game.managers.ScreenManager.Param.SCORE

class GameOverScreen(params: Map<ScreenManager.Param, Any>) : Screen {

    private var manager = params[ASSET_MANAGER] as AssetManager
    private var adsController = params[SERVICES_CONTROLLER] as AdsController
    private var score = params[SCORE] as Int
    private val camera = OrthographicCamera(Config.WIDTH_GAME, Config.HEIGHT_GAME)
    private val stage = Stage(ScreenViewport(camera))

    init {
        Gdx.input.inputProcessor = stage
        //adsController.showInterstitialAd()
        adsController.showBannerAd()
        //adsController.showVideoAd()
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
        val isWinning = score >= Config.AchievementScore.FINISH_GAME.score
        val background = Background(manager)
        val finalAction = if (isWinning.not()) CookieRests(manager) else Together(manager)
        val restartIcon = RestartIcon(manager)
        val shadow = Shadow(manager)
        val scores = Scores(manager, score)

        stage.apply {
            addActor(background)
            addActor(finalAction)
            addActor(restartIcon)
            addActor(shadow)
            addActor(scores)
        }

        restartIcon.animate(SHOW_ON_SCENE)
        shadow.animate(SHOW_ON_SCENE)
        scores.animate(SHOW_ON_SCENE)
        AudioManager.play(MAIN_MENU_MUSIC)

        addClickListener(restartIcon) {
            adsController.hideBannerAd()
            AudioManager.stopAll()
            restartIcon.animate(HIDE_FROM_SCENE)
            scores.animate(HIDE_FROM_SCENE)
            shadow.animate(HIDE_FROM_SCENE, Runnable {
                ScreenManager.setScreen(GAME_SCREEN)
            })
        }

/*        addClickListener(mainMenuIcon) {
            title.animate(HIDE_FROM_SCENE)
            restartIcon.animate(HIDE_FROM_SCENE)
            mainMenuIcon.animate(HIDE_FROM_SCENE)
            scores.animate(HIDE_FROM_SCENE)
            shadow.animate(HIDE_FROM_SCENE, Runnable {
                ScreenManager.setScreen(START_SCREEN)
            })
        }*/
    }


    private fun addClickListener(actor: Actor, function: () -> Unit) {
        actor.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                AudioManager.play(CLICK_SOUND)
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