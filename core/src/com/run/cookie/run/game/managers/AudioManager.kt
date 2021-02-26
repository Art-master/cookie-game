/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.MusicLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.Prefs
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object AudioManager {

    interface Audio

    enum class SoundApp(fileName: String, val volume: Float = 1f) : Audio {
        CLICK_SOUND("click.mp3", 0.1f),
        CRUNCH("crunch.mp3", 0.1f),
        JUMP_ON_BOX("jump_on_box.mp3"),
        JUMP("jump.ogg", 0.1f),
        SCORE("score.wav", 0.3f),
        DOOR_SQUEAK("door_squeak.mp3", 0.3f),
        GUN_SHOT("gun_shot.wav", 0.3f);

        var descriptor: AssetDescriptor<Sound> =
                AssetDescriptor("${Config.SOUNDS_FOLDER}/${fileName}", Sound::class.java)

        var resource: Sound? = null

    }

    enum class MusicApp(fileName: String, val volume: Float = 1f) : Audio {
        GAME_MUSIC("gameMusic.mp3", 0.3f),
        MAIN_MENU_MUSIC("mainMenuMusic.mp3", 0.3f);

        var descriptor: AssetDescriptor<Music> =
                AssetDescriptor("${Config.SOUNDS_FOLDER}/${fileName}", Music::class.java)

        var resource: Music? = null
    }

    private val prefs = Gdx.app.getPreferences(Prefs.NAME)

    var isSoundEnable = prefs.getBoolean(Prefs.SOUND, true)
        private set(isEnable) {
            field = isEnable
            prefs.putBoolean(Prefs.SOUND, isEnable)
            prefs.flush()
        }

    var isMusicEnable = prefs.getBoolean(Prefs.MUSIC, true)
        private set(isEnable) {
            field = isEnable
            prefs.putBoolean(Prefs.MUSIC, isEnable)
            prefs.flush()
        }

    fun switchSoundSetting() {
        isSoundEnable = isSoundEnable.not()
        if (isSoundEnable.not()) stopAllSounds()
    }

    fun switchMusicSetting() {
        isMusicEnable = isMusicEnable.not()
        if (isMusicEnable.not()) stopAllMusics()
    }

    fun play(audio: Audio, isLooping: Boolean = false) {
        if (audio is SoundApp) {
            if (isSoundEnable.not()) return
            playSound(audio)

        } else if (audio is MusicApp) {
            if (isMusicEnable.not()) return
            audio.resource?.apply {
                play()
                this.isLooping = isLooping
                volume = audio.volume
            }
        }
    }

    /**
     * HACK: If don't use a different thread, graphics on the screen will be twitch a little
     */
    private fun playSound(audio: SoundApp) {
        GlobalScope.launch {
            audio.resource?.play(audio.volume)
        }
    }

    fun stop(audio: Audio) {
        if (audio is SoundApp) {
            audio.resource?.stop()
        } else if (audio is MusicApp) {
            audio.resource?.stop()
        }
    }

    fun stopAll() {
        stopAllMusics()
        stopAllSounds()
    }

    fun stopAllMusics() {
        for (audio in MusicApp.values()) {
            audio.resource?.apply {
                stop()
                isLooping = false
            }
        }
    }

    fun stopAllSounds() {
        for (sound in SoundApp.values()) {
            sound.resource?.stop()
        }
    }

    fun loadSounds(manager: AssetManager) {
        val resolver: FileHandleResolver = InternalFileHandleResolver()
        val soundLoader = MusicLoader(resolver)
        manager.setLoader(Music::class.java, soundLoader)

        for (sound in SoundApp.values()) {
            manager.load(sound.descriptor)
        }
    }

    fun loadMusic(manager: AssetManager) {
        val resolver: FileHandleResolver = InternalFileHandleResolver()
        val musicLoader = MusicLoader(resolver)
        manager.setLoader(Music::class.java, musicLoader)

        for (music in MusicApp.values()) {
            manager.load(music.descriptor)
        }
    }

    fun onMusicsLoaded(manager: AssetManager) {
        for (music in MusicApp.values()) {
            music.resource = manager.get(music.descriptor)
        }
    }

    fun onSoundsLoaded(manager: AssetManager) {
        for (sound in SoundApp.values()) {
            sound.resource = manager.get(sound.descriptor)
        }
    }
}