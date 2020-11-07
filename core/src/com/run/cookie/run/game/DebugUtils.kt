package com.run.cookie.run.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Timer
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors

object DebugUtils {
    fun drawVerticalLine(batch: Batch?, manager: AssetManager, x: Float) {
        if (!Config.Debug.COOKIE_POSITION.state) return
        val texture = manager.get(Descriptors.environment)
        val region = texture.findRegion(Assets.EnvironmentAtlas.SHADOW)
        batch?.draw(region, x, 0f, 1f, Config.HEIGHT_GAME)
    }

    fun startPeriodicTimer(delaySeconds: Float = 0f, intervalSeconds: Float = 0f, func: () -> Any) {
        Timer.schedule(object : Timer.Task() {
            override fun run() {
                func.invoke()
            }
        }, delaySeconds, intervalSeconds)
    }
}