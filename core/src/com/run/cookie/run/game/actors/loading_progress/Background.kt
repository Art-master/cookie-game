/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.loading_progress

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import com.badlogic.gdx.graphics.Color
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.GameActor

class Background(manager : AssetManager) : GameActor() {
    private val progressAtlas = manager.get(Descriptors.progressBar)
    private val whiteSquareRegion = progressAtlas.findRegion(Assets.ProgressAtlas.WHITE_SQUARE)

    init {
        width = Config.WIDTH_GAME
        height = Config.HEIGHT_GAME
        x = 0f
        y = 0f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        drawBackground(batch)
    }

    private fun drawBackground(batch: Batch){
        batch.draw(whiteSquareRegion, x, y, width, height)
    }
}