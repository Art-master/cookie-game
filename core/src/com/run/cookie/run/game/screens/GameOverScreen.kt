package com.run.cookie.run.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.actors.game_over_screen.*
import com.run.cookie.run.game.api.AnimationType.*
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.managers.AudioManager
import com.run.cookie.run.game.managers.AudioManager.MusicApp.MAIN_MENU_MUSIC
import com.run.cookie.run.game.managers.AudioManager.SoundApp.CLICK_SOUND
import com.run.cookie.run.game.managers.ScreenManager
import com.run.cookie.run.game.managers.ScreenManager.Param.*
import com.run.cookie.run.game.managers.ScreenManager.Screens.GAME_SCREEN
import com.run.cookie.run.game.managers.ScreenManager.Screens.MAIN_MENU_SCREEN
import com.run.cookie.run.game.managers.VibrationManager
import com.run.cookie.run.game.services.ServicesController

class GameOverScreen(params: Map<ScreenManager.Param, Any>) : GameScreen(params) {

    private var controller = params[SERVICES_CONTROLLER] as ServicesController
    private var score = params[SCORE] as Int
    private var wasWinGame = params[WAS_WIN_GAME] as Boolean?

    init {
        Gdx.input.inputProcessor = stage
        //adsController.showInterstitialAd()
        adsController.showBannerAd()
        //adsController.showVideoAd()
        if (controller.isSignedIn()) controller.submitScore(score.toLong())
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
        val finalAction = if (wasWinGame == true) Together(manager) else CookieRests(manager)
        val restartIcon = RestartIcon(manager)
        val cup = Cup(manager)
        val scores = Scores(manager, score)
        val topScores = GameOverMenuIcon(manager, 40f, 780f, Assets.MainMenuAtlas.TOP_SCORES)
        val awards = GameOverMenuIcon(manager, 70f, 580f, Assets.MainMenuAtlas.AWARDS)
        val share = GameOverMenuIcon(manager, 1777f, 780f, Assets.MainMenuAtlas.SHARE, false)
        val mainMenu = GameOverMenuIcon(manager, 1777f, 580f, Assets.MainMenuAtlas.MAIN_MENU, false)

        stageBackground.addActor(background)
        stage.apply {
            addActor(cup)
            addActor(finalAction)
            addActor(restartIcon)
            addActor(scores)
            if (controller.isSignedIn() || Config.Debug.PLAY_SERVICES.state) {
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

        addClickListener(topScores) {
            controller.showLeaderboard()
            topScores.animate(CLICK)
        }
        addClickListener(awards) {
            controller.showAllAchievements()
            awards.animate(CLICK)
        }
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
        super.dispose()
    }
}