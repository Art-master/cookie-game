package com.mygdx.game.managers

import com.badlogic.gdx.Gdx
import com.mygdx.game.Config
import com.mygdx.game.Prefs

object VibrationManager {
    private var prefs = Gdx.app.getPreferences(Prefs.NAME)

    var isVibrationEnable = prefs.getBoolean(Prefs.VIBRATION, true)
        private set(isEnable) {
            field = isEnable
            prefs.putBoolean(Prefs.VIBRATION, isEnable)
            prefs.flush()
        }

    fun switchVibrationSetting() {
        isVibrationEnable = isVibrationEnable.not()
    }

    fun vibrate() {
        if (isVibrationEnable) {
            Gdx.input.vibrate(Config.VIBRATION_TIME_MS)
        }
    }
}