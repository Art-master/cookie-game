/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.data.Descriptors
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.Assets

class Background(manager: AssetManager) : GameActor() {
    private val environmentAtlas = manager.get(Descriptors.environment)
    private val white = environmentAtlas.findRegion(Assets.EnvironmentAtlas.WHITE)

    init {
        width = Config.WIDTH_GAME
        height = Config.HEIGHT_GAME
        x = 0f
        y = 0f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(white, x,  y, width, height)
    }
}