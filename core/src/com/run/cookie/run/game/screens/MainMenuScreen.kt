package com.run.cookie.run.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.actors.main_menu_screen.*
import com.run.cookie.run.game.api.AnimationType
import com.run.cookie.run.game.managers.AudioManager
import com.run.cookie.run.game.managers.AudioManager.MusicApp
import com.run.cookie.run.game.managers.AudioManager.SoundApp
import com.run.cookie.run.game.managers.ScreenManager
import com.run.cookie.run.game.managers.ScreenManager.Param.*
import com.run.cookie.run.game.managers.ScreenManager.Screens.*
import com.run.cookie.run.game.managers.VibrationManager

class MainMenuScreen(params: Map<ScreenManager.Param, Any>) : GameScreen(params) {

    private var firstAppRun = params[FIRST_APP_RUN] as Boolean

    init {
        Gdx.input.inputProcessor = stage
        if (firstAppRun.not()) adsController.showBannerAd()
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
        val title = MainTitle(manager)
        val musicIcon = MusicIcon(manager)
        val soundIcon = SoundIcon(manager, musicIcon)
        val vibrationIcon = VibrationIcon(manager, musicIcon)
        val cookieStoryIcon = CookieStoryIcon(manager, soundIcon)
        val playButton = PlayButton(manager)
        stageBackground.addActor(background)
        stage.apply {
            addActor(title)
            addActor(musicIcon)
            addActor(soundIcon)
            addActor(vibrationIcon)
            if (firstAppRun.not()) addActor(cookieStoryIcon)
            addActor(playButton)
        }

        title.animate(AnimationType.SHOW_ON_SCENE)
        musicIcon.animate(AnimationType.SHOW_ON_SCENE)
        soundIcon.animate(AnimationType.SHOW_ON_SCENE)
        vibrationIcon.animate(AnimationType.SHOW_ON_SCENE)
        cookieStoryIcon.animate(AnimationType.SHOW_ON_SCENE)
        playButton.animate(AnimationType.SHOW_ON_SCENE)
        shadow.animate(AnimationType.SHOW_ON_SCENE)

        addClickListener(musicIcon) {
            AudioManager.switchMusicSetting()
            AudioManager.play(MusicApp.MAIN_MENU_MUSIC)
            AudioManager.play(SoundApp.CLICK_SOUND)
            musicIcon.changeBackground()
        }

        addClickListener(soundIcon) {
            AudioManager.switchSoundSetting()
            AudioManager.play(MusicApp.MAIN_MENU_MUSIC)
            AudioManager.play(SoundApp.CLICK_SOUND)
            soundIcon.changeBackground()
        }

        addClickListener(vibrationIcon) {
            VibrationManager.switchVibrationSetting()
            AudioManager.play(SoundApp.CLICK_SOUND)
            vibrationIcon.changeBackground()
        }

        addClickListener(cookieStoryIcon) {
            AudioManager.play(SoundApp.CLICK_SOUND)
            ScreenManager.setScreen(COMICS_SCREEN, Pair(SCREEN_LINK, MAIN_MENU_SCREEN))
            if (firstAppRun.not()) adsController.hideBannerAd()
        }

        addClickListener(playButton) {
            AudioManager.play(SoundApp.CRUNCH)
            if (firstAppRun.not()) adsController.hideBannerAd()
            playButton.animate(AnimationType.HIDE_FROM_SCENE)
            title.animate(AnimationType.HIDE_FROM_SCENE)
            musicIcon.animate(AnimationType.HIDE_FROM_SCENE)
            soundIcon.animate(AnimationType.HIDE_FROM_SCENE)
            vibrationIcon.animate(AnimationType.HIDE_FROM_SCENE)
            cookieStoryIcon.animate(AnimationType.HIDE_FROM_SCENE)
            shadow.animate(AnimationType.HIDE_FROM_SCENE, Runnable {
                setScreen()
                AudioManager.stopAll()
            })
        }

        AudioManager.play(MusicApp.MAIN_MENU_MUSIC, true)
    }

    private fun setScreen() {
        if (firstAppRun || Config.Debug.ALWAYS_SHOW_STORY.state) {
            ScreenManager.setScreen(COMICS_SCREEN)
        } else ScreenManager.setScreen(GAME_SCREEN)
    }

    private fun addClickListener(actor: Actor, function: () -> Unit) {
        actor.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                function()
                VibrationManager.vibrate()
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