package com.mygdx.game.managers

import com.badlogic.gdx.Gdx
import com.mygdx.game.Prefs

object VibrationManager {
    private var prefs = Gdx.app.getPreferences(Prefs.NAME)

    enum class VibrationType(val signature: LongArray) {
        CLICK(longArrayOf(5L, 50L)),
        ACTOR_CATCH(longArrayOf(30L, 30L, 30L, 30L, 30L, 30L, 30L, 30L, 30L, 30L, 30L, 30L))
    }

    var isVibrationEnable = prefs.getBoolean(Prefs.VIBRATION, true)
        private set(isEnable) {
            field = isEnable
            prefs.putBoolean(Prefs.VIBRATION, isEnable)
            prefs.flush()
        }

    fun switchVibrationSetting() {
        isVibrationEnable = isVibrationEnable.not()
    }

    fun vibrate(type: VibrationType = VibrationType.CLICK, repeatCount: Int = -1) {
        if (isVibrationEnable) {
            Gdx.input.vibrate(type.signature, repeatCount)
        }
    }

    fun cancel() {
        if (isVibrationEnable) Gdx.input.cancelVibrate()
    }
}