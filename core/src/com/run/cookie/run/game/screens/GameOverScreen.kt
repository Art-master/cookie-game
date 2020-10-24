package com.run.cookie.run.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.managers.ScreenManager
import com.run.cookie.run.game.managers.ScreenManager.Screens.*
import com.run.cookie.run.game.actors.Shadow
import com.run.cookie.run.game.actors.game_over_screen.*
import com.run.cookie.run.game.services.AdsController
import com.run.cookie.run.game.api.AnimationType.*
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.managers.AudioManager
import com.run.cookie.run.game.managers.AudioManager.MusicApp.*
import com.run.cookie.run.game.managers.AudioManager.SoundApp.*
import com.run.cookie.run.game.managers.ScreenManager.Param.*
import com.run.cookie.run.game.managers.ScreenManager.Param.SCORE
import com.run.cookie.run.game.managers.VibrationManager
import com.run.cookie.run.game.services.ServicesController

class GameOverScreen(params: Map<ScreenManager.Param, Any>) : Screen {

    private var manager = params[ASSET_MANAGER] as AssetManager
    private var adsController = params[SERVICES_CONTROLLER] as AdsController
    private var controller = params[SERVICES_CONTROLLER] as ServicesController
    private var score = params[SCORE] as Int
    private var wasWinGame = params[WAS_WIN_GAME] as Boolean?
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
        val background = Background(manager)
        val finalAction = if (wasWinGame == true) Together(manager) else CookieRests(manager)
        val restartIcon = RestartIcon(manager)
        val shadow = Shadow(manager)
        val scores = Scores(manager, score)
        val topScores = GameOverMenuIcon(manager, 40f, 780f, Assets.MainMenuAtlas.TOP_SCORES)
        val awards = GameOverMenuIcon(manager, 70f, 580f, Assets.MainMenuAtlas.AWARDS)
        val share = GameOverMenuIcon(manager, 1777f, 780f, Assets.MainMenuAtlas.SHARE, false)
        val mainMenu = GameOverMenuIcon(manager, 1777f, 580f, Assets.MainMenuAtlas.MAIN_MENU, false)

        stage.apply {
            addActor(background)
            addActor(finalAction)
            addActor(restartIcon)
            addActor(shadow)
            addActor(scores)
            if(!controller.isSignedIn()){ //TODO after tests delete inversion
                addActor(topScores)
                addActor(awards)
            }
            addActor(share)
            addActor(mainMenu)
        }

        restartIcon.animate(SHOW_ON_SCENE)
        shadow.animate(SHOW_ON_SCENE)
        scores.animate(SHOW_ON_SCENE)
        topScores.animate(SHOW_ON_SCENE)
        awards.animate(SHOW_ON_SCENE)
        share.animate(SHOW_ON_SCENE)
        mainMenu.animate(SHOW_ON_SCENE)
        AudioManager.play(MAIN_MENU_MUSIC)

        addClickListener(restartIcon) {
            adsController.hideBannerAd()
            AudioManager.stopAll()
            restartIcon.animate(HIDE_FROM_SCENE)
            scores.animate(HIDE_FROM_SCENE)
            topScores.animate(HIDE_FROM_SCENE)
            awards.animate(HIDE_FROM_SCENE)
            share.animate(HIDE_FROM_SCENE)
            mainMenu.animate(HIDE_FROM_SCENE)
            shadow.animate(HIDE_FROM_SCENE, Runnable {
                ScreenManager.setScreen(GAME_SCREEN)
            })
        }

        addClickListener(topScores) { topScores.animate(CLICK) }
        addClickListener(awards) { awards.animate(CLICK) }
        addClickListener(share) {
            share.animate(CLICK)
            controller.share(score)
        }
        addClickListener(mainMenu) {
            AudioManager.stopAll()
            restartIcon.animate(HIDE_FROM_SCENE)
            scores.animate(HIDE_FROM_SCENE)
            topScores.animate(HIDE_FROM_SCENE)
            awards.animate(HIDE_FROM_SCENE)
            share.animate(HIDE_FROM_SCENE)
            mainMenu.animate(HIDE_FROM_SCENE)
            shadow.animate(HIDE_FROM_SCENE, Runnable {
                ScreenManager.setScreen(MAIN_MENU_SCREEN)
            })
        }
    }


    private fun addClickListener(actor: Actor, function: () -> Unit) {
        actor.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                AudioManager.play(CLICK_SOUND)
                adsController.hideBannerAd()
                VibrationManager.vibrate()
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