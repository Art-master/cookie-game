package com.run.cookie.run.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.run.cookie.run.game.actors.game_over_screen.*
import com.run.cookie.run.game.api.Advertising
import com.run.cookie.run.game.api.Advertising.AdType
import com.run.cookie.run.game.api.Advertising.Adv
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
import com.run.cookie.run.game.services.AdsCallback
import com.run.cookie.run.game.services.ServicesController
import java.util.*

class GameOverScreen(params: Map<ScreenManager.Param, Any>) : GameScreen(params) {

    private var controller = params[SERVICES_CONTROLLER] as ServicesController
    private var score = params[SCORE] as Int
    private var advertising = params[AD] as Advertising

    private val scoresActor = Scores(manager, score)

    private var wasWinGame = params[WAS_WIN_GAME] as Boolean?

    private var newScreen: ScreenManager.Screens? = null

    init {
        Gdx.input.inputProcessor = stage
        adsController.showBannerAd()
        if (controller.isSignedIn() && score > scoresActor.bestScoreNum) {
            controller.submitScore(score.toLong())
        }
        ScreenManager.setGlobalParameter(FIRST_APP_RUN, false)
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if (stage.actors.isEmpty) addActorsToStage()
        applyStages(delta)

        newScreen?.let {
            ScreenManager.setScreen(newScreen!!)
            newScreen = null
        }
    }

    private fun addActorsToStage() {
        val background = Background(manager)
        val finalAction = if (wasWinGame == true) Together(manager) else CookieRests(manager)
        val restartIcon = RestartIcon(manager)
        val cup = Cup(manager)
        val topScores = GameOverMenuIcon(manager, 40f, 780f, Assets.MainMenuAtlas.TOP_SCORES)
        val awards = GameOverMenuIcon(manager, 70f, 580f, Assets.MainMenuAtlas.AWARDS)
        val share = GameOverMenuIcon(manager, 1777f, 780f, Assets.MainMenuAtlas.SHARE, false)
        val mainMenu = GameOverMenuIcon(manager, 1777f, 580f, Assets.MainMenuAtlas.MAIN_MENU, false)

        stageBackground.addActor(background)
        stage.apply {
            addActor(cup)
            addActor(finalAction)
            addActor(restartIcon)
            addActor(scoresActor)
            if (adsController.isNetworkAvailable()) {
                addActor(topScores)
                addActor(awards)
            }
            addActor(share)
            addActor(mainMenu)
        }

        restartIcon.animate(SHOW_ON_SCENE)
        shadow.animate(SHOW_ON_SCENE)
        scoresActor.animate(SHOW_ON_SCENE)
        topScores.animate(SHOW_ON_SCENE)
        awards.animate(SHOW_ON_SCENE)
        share.animate(SHOW_ON_SCENE)
        mainMenu.animate(SHOW_ON_SCENE)
        AudioManager.play(MAIN_MENU_MUSIC)

        addClickListener(restartIcon) {
            adsController.hideBannerAd()
            AudioManager.stopAll()
            restartIcon.animate(HIDE_FROM_SCENE)
            scoresActor.animate(HIDE_FROM_SCENE)
            topScores.animate(HIDE_FROM_SCENE)
            awards.animate(HIDE_FROM_SCENE)
            share.animate(HIDE_FROM_SCENE)
            mainMenu.animate(HIDE_FROM_SCENE)
            shadow.animate(HIDE_FROM_SCENE, Runnable {
                onNewScreen(GAME_SCREEN)
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
            adsController.hideBannerAd()
            restartIcon.animate(HIDE_FROM_SCENE)
            scoresActor.animate(HIDE_FROM_SCENE)
            topScores.animate(HIDE_FROM_SCENE)
            awards.animate(HIDE_FROM_SCENE)
            share.animate(HIDE_FROM_SCENE)
            mainMenu.animate(HIDE_FROM_SCENE)
            shadow.animate(HIDE_FROM_SCENE, Runnable {
                onNewScreen(MAIN_MENU_SCREEN)
            })
        }
    }

    private fun onNewScreen(screen: ScreenManager.Screens) {
        if (adsController.isNetworkAvailable()) {
            showAddIfNeedAndSetScreenAfter(
                    object : AdsCallback {
                        override fun close() {
                            newScreen = screen
                        }

                        override fun click() {
                            advertising.commonClickCount++
                            newScreen = screen
                        }

                        override fun fail() {
                            newScreen = screen
                        }

                    }, screen)
        } else newScreen = screen
    }

    private fun showAddIfNeedAndSetScreenAfter(callback: AdsCallback, screen: ScreenManager.Screens) {
        val lastAd = advertising.last
        val minCountOneByOne = 2

        Timer().schedule(object : TimerTask() {
            override fun run() {
                newScreen = screen
            }
        }, 2 * 1000)


        if (advertising.commonClickCount > 5) callback.close()

        if (lastAd.type == AdType.NONE && lastAd.lastCountOneByOne == minCountOneByOne) {
            lastAd.lastCountOneByOne = 0
            advertising.last = Adv(AdType.INTERSTITIAL)
            adsController.showInterstitialAd(callback)
        } else {
            if (lastAd.type != AdType.NONE) {
                advertising.last = Adv()
            } else lastAd.lastCountOneByOne++
            advertising.last.timeMs = System.currentTimeMillis()
            callback.close()
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