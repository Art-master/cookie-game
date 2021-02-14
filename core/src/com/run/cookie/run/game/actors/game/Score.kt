/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.Animated
import com.run.cookie.run.game.api.AnimationType
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.FontParam
import com.run.cookie.run.game.managers.VibrationManager

class Score(manager: AssetManager) : GameActor(), Animated {
    private var score: BitmapFont = manager.get(FontParam.SCORE.fontName)
    private val matrix = Matrix4()

    var scoreNum = 0
        set(value) {
            animate(AnimationType.SCORE_INCREASE)
            VibrationManager.vibrate()
            if (value <= Config.Achievement.FINISH_GAME.score) field = value
        }

    private val symbolSize = 50
    private val paddingX = 300

    init {
        y = Config.HEIGHT_GAME - 50f
        setScale(1f)
    }

    override fun act(delta: Float) {
        super.act(delta)

        x = Config.WIDTH_GAME - (symbolSize * scoreNum.toString().length) - paddingX
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        matrix.idt()
        matrix.scale(scaleX, scaleY, 1f)

        batch!!.transformMatrix = matrix
        score.draw(batch, scoreNum.toString(), x, y)
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        if (type == AnimationType.SCORE_INCREASE) {
            val animDuration = 0.1f
            addAction(Actions.sequence(
                    Actions.scaleTo(1.01f, 1.01f, animDuration),
                    Actions.scaleTo(1f, 1f, animDuration)))
        }
    }
}