package com.mygdx.game.actors.main_menu_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.Config
import com.mygdx.game.actors.Animated

class MainTitle(manager : AssetManager) : Actor(), Animated {
    private val texture = manager.get(Descriptors.menu)
    private val region = texture.findRegion(Assets.MainMenuAtlas.TITLE)

    private var centerX = 0f
    private var centerY = 0f


    init {
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()
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
        batch.draw(region, x, y, width, height)
    }

    override fun animate(isRevert: Boolean, runAfter: Runnable) {
        val animDuration = 0.5f
        val moveToOutside = Actions.moveTo(x, Gdx.graphics.height.toFloat(), animDuration, Interpolation.exp10)
        addAction(moveToOutside)
    }
}