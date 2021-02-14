/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors

class Shadow(manager : AssetManager) : GameActor() {
    private val texture = manager.get(Descriptors.progressBar)
    private var region = texture.findRegion(Assets.ProgressAtlas.BLACK_SQUARE)

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.setColor(color.r, color.g, color.b, 0.5f)
        batch.draw(region, 0f, 0f, Config.WIDTH_GAME, Config.HEIGHT_GAME)
        batch.setColor(color.r, color.g, color.b, 1f)
    }
}