/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.main_menu_screen

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors

class Background(manager: AssetManager) : GameActor() {
    private val backgroundTexture = manager.get(Descriptors.background)
    private val textureMenu = manager.get(Descriptors.menu)
    private val blurRegion = textureMenu.findRegion(Assets.MainMenuAtlas.BLUR)

    init {
        width = Config.WIDTH_GAME
        height = Config.HEIGHT_GAME
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        drawBackground(batch)
        drawBlur(batch)
    }

    private fun drawBackground(batch: Batch) {
        batch.draw(backgroundTexture, 0f, 0f, width, height)
    }

    private fun drawBlur(batch: Batch) {
        batch.draw(blurRegion, 0f, 0f, width, height)
    }
}