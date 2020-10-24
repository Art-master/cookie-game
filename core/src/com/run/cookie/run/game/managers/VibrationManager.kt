package com.run.cookie.run.game.managers

import com.badlogic.gdx.Gdx
import com.run.cookie.run.game.Prefs
import java.util.*

object VibrationManager {
    private var prefs = Gdx.app.getPreferences(Prefs.NAME)
    private var time = 0L

    enum class VibrationType(val signature: LongArray, val repeat: Int = -1) {
        CLICK(longArrayOf(5L, 50L)),
        ACTOR_CATCH(longArrayOf(30L, 30L, 30L, 30L, 30L, 30L, 30L, 30L, 30L, 30L, 30L, 30L)),
        STICKY_ITEM(longArrayOf(100L, 1L), 0),
        STICKY_SLIP(longArrayOf(150L, 1L), 0),
        BOOM(longArrayOf(1L, 500L)),
        ACTOR_FALL(longArrayOf(1L, 500L));

        fun get() = signature.reduce { acc, v -> acc + v }
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
        if (isVibrationEnable && time == 0L) {
            time = type.get()
            val vibrationTask = object : TimerTask() {
                override fun run() {
                    time = if (repeatCount == -1) 0L else 1L
                }
            }
            Timer().schedule(vibrationTask, time)
            Gdx.input.vibrate(type.signature, if (repeatCount < 0) type.repeat else repeatCount)
        }
    }

    fun cancel() {
        if (isVibrationEnable) Gdx.input.cancelVibrate()
        time = 0L
    }
}