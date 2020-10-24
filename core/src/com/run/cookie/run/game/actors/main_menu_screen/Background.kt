package com.run.cookie.run.game.actors.main_menu_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors
import com.badlogic.gdx.graphics.Color
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.GameActor

class Background(manager : AssetManager) : GameActor() {
    private val backgroundTexture = manager.get(Descriptors.background)
    //private val textureOther = manager.get(Descriptors.environment)
    private val textureMenu = manager.get(Descriptors.menu)
    private val blurRegion = textureMenu.findRegion(Assets.MainMenuAtlas.BLUR)

    private var centerX = 0f
    private var centerY = 0f


    init {
        width = blurRegion.originalWidth.toFloat()
        height = blurRegion.originalHeight.toFloat()
        x = (Config.WIDTH_GAME / 2) - width / 2
        y = (Config.HEIGHT_GAME - height - 50)
    }

    override fun act(delta: Float) {
        super.act(delta)

        centerX = x + (width / 2)
        centerY = y + (height / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        drawBackground(batch)
        drawBlur(batch)
    }

    private fun drawBackground(batch: Batch){
        batch.draw(backgroundTexture, 0f, 0f, Gdx.graphics.width.toFloat(),
                Gdx.graphics.height.toFloat())
    }

    private fun drawBlur(batch: Batch){
        batch.draw(blurRegion, 0f, 0f, Gdx.graphics.width.toFloat(),
                Gdx.graphics.height.toFloat())
    }
}