/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.game_over_screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors

class CookieRests(manager : AssetManager) : GameActor() {
    private val texture = manager.get(Descriptors.cookie)
    private val region = texture.findRegion(Assets.CookieAtlas.COOKIE_RESTS)

    init {
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()
        x = 910f
        y = 200f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(region, x, y, width, height)
    }
}