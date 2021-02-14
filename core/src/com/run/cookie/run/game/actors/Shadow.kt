/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.Config.SHADOW_ANIMATION_TIME_S
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import com.run.cookie.run.game.api.Animated
import com.run.cookie.run.game.api.AnimationType
import com.run.cookie.run.game.api.GameActor

class Shadow(val manager : AssetManager) : GameActor(), Animated {
    private val texture = manager.get(Descriptors.progressBar)
    private var region = texture.findRegion(Assets.ProgressAtlas.BLACK_SQUARE)

    init {
        x = 0f
        y = 0f
        width = Config.WIDTH_GAME
        height = Config.HEIGHT_GAME

        setColor(color.r, color.g, color.b, 0f)
        touchable = Touchable.disabled
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE

        batch.color = color
        batch.draw(region, x, y, width, height)
        batch.setColor(color.r, color.g, color.b, 1f)
    }

    fun invertColor(){
        region = manager.get(Descriptors.environment).findRegion(Assets.EnvironmentAtlas.WHITE)
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = SHADOW_ANIMATION_TIME_S
        val action = when(type) {
            AnimationType.HIDE_FROM_SCENE -> Actions.alpha(1f, animDuration)
            AnimationType.SHOW_ON_SCENE -> {
                val color = color
                setColor(color.r, color.g, color.b, 1f)
                Actions.alpha(0f, animDuration)
            }
            else -> return
        }
        val run = Actions.run(runAfter)
        val sequence = Actions.sequence(action, run)
        addAction(sequence)
    }
}