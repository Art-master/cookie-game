package com.run.cookie.run.game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.MusicLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.run.cookie.run.game.Prefs
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.Array
import com.run.cookie.run.game.Config

object AudioManager {

    interface Audio

    enum class SoundApp(val descriptor: AssetDescriptor<Sound>, val volume: Float = 1f) : Audio {
        CLICK_SOUND(AssetDescriptor("${Config.SOUNDS_FOLDER}/click.mp3", Sound::class.java), 0.3f),
        CRUNCH(AssetDescriptor("${Config.SOUNDS_FOLDER}/crunch.mp3", Sound::class.java), 0.3f),
        JUMP_ON_BOX( AssetDescriptor("${Config.SOUNDS_FOLDER}/jump_on_box.wav", Sound::class.java)),
        JUMP(AssetDescriptor("${Config.SOUNDS_FOLDER}/jump.wav", Sound::class.java)),
        SCORE(AssetDescriptor("${Config.SOUNDS_FOLDER}/score.wav", Sound::class.java),0.3f)
    }

    enum class MusicApp(val descriptor: AssetDescriptor<Music>, val volume: Float = 1f) : Audio {
        GAME_MUSIC(AssetDescriptor("${Config.SOUNDS_FOLDER}/gameMusic.mp3", Music::class.java),0.3f),
        MAIN_MENU_MUSIC(AssetDescriptor("${Config.SOUNDS_FOLDER}/mainMenuMusic.mp3", Music::class.java)),
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
        if (isSoundEnable.not()) stopAll()
    }

    fun switchMusicSetting() {
        isMusicEnable = isMusicEnable.not()
        if (isMusicEnable.not()) stopAll()
    }

    fun play(audio: Audio, isLooping: Boolean = false) {
        val manager = ScreenManager.globalParameters[ScreenManager.Param.ASSET_MANAGER] as AssetManager
        if (audio is SoundApp) {
            if (isSoundEnable.not()) return
            manager.get(audio.descriptor)?.play(audio.volume)

        } else if (audio is MusicApp) {
            if (isMusicEnable.not()) return
            manager.get(audio.descriptor)?.apply {
                play()
                this.isLooping = isLooping
                volume = audio.volume
            }
        }
    }

    fun stop(audio: Audio) {
        val manager = ScreenManager.globalParameters[ScreenManager.Param.ASSET_MANAGER] as AssetManager
        if (audio is SoundApp) {
            manager.get(audio.descriptor)?.stop()
        } else if (audio is MusicApp) {
            manager.get(audio.descriptor)?.stop()
        }
    }

    fun stopAll() {
        val manager = ScreenManager.globalParameters[ScreenManager.Param.ASSET_MANAGER] as AssetManager
        for (sound in manager.getAll(Sound::class.java, Array())) {
            sound?.stop()
        }
        for (music in manager.getAll(Music::class.java, Array())) {
            music?.apply {
                stop()
                isLooping = false
            }
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
}