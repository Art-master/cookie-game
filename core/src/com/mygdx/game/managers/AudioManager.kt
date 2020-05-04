package com.mygdx.game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.utils.GdxRuntimeException
import com.mygdx.game.Config
import com.mygdx.game.Prefs

object AudioManager {

    interface Audio
    enum class Sound(val fileName: String, val volume: Float = 1f) : Audio {
        NONE("", 0f),
        CLICK_SOUND("click.mp3", 0.3f),
        CRUNCH("crunch.mp3", 0.3f),
        JUMP_ON_BOX("jump_on_box.wav"),
        JUMP("jump.wav"),
        SCORE("score.wav", 0.3f)
    }

    enum class MusicApp(val fileName: String, val volume: Float = 1f) : Audio {
        GAME_MUSIC("gameMusic.mp3", 0.3f),
        MAIN_MENU_MUSIC("mainMenuMusic.mp3"),
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

    private val sounds = HashMap<String, com.badlogic.gdx.audio.Sound?>()
    private val music = HashMap<String, Music?>()

    init {
        for(sound in Sound.values()){
            sounds[sound.name] = getSound(sound)
        }

        for(music in MusicApp.values()){
            this.music[music.name] = getMusic(music)
        }
    }

    private fun getSound(sound: Sound): com.badlogic.gdx.audio.Sound? {
        var audio: com.badlogic.gdx.audio.Sound? = null
        try {
            val file = Gdx.files.internal("${Config.SOUNDS_FOLDER}/${sound.fileName}")
            audio = Gdx.audio.newSound(file)
        } catch (e: GdxRuntimeException){
            Gdx.app.log("Sound file load error", e.message)
        }
        return audio
    }

    private fun getMusic(music: MusicApp): Music? {
        var audio: Music? = null
        try {
            val file = Gdx.files.internal("${Config.SOUNDS_FOLDER}/${music.fileName}")
            audio = Gdx.audio.newMusic(file)
        } catch (e: GdxRuntimeException){
            Gdx.app.log("Music file load error", e.message)
        }
        return audio
    }

    fun switchSoundSetting() {
        isSoundEnable = isSoundEnable.not()
        if(isSoundEnable.not()) stopAll()
    }

    fun switchMusicSetting() {
        isMusicEnable = isMusicEnable.not()
        if(isMusicEnable.not()) stopAll()
    }

    fun play(audio: Audio, isLooping: Boolean = false) {
        if(audio is Sound){
            if(isSoundEnable.not()) return
            sounds[audio.name]?.play(audio.volume)
        }else if(audio is MusicApp){
            if(isMusicEnable.not()) return
            music[audio.name]?.apply {
                play()
                this.isLooping = isLooping
                volume = audio.volume
            }
        }
    }

    fun stop(audio: Audio) {
        if(audio is Sound){
            sounds[audio.name]?.stop()
        }else if(audio is MusicApp){
            music[audio.name]?.stop()
        }
    }

    fun stopAll() {
        for(sound in sounds.values){
            sound?.stop()
        }
        for(music in music.values){
            music?.apply {
                stop()
                isLooping = false
            }
        }
    }
}