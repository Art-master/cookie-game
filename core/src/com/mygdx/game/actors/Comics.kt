package com.mygdx.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.Config.SHADOW_ANIMATION_TIME_S
import com.mygdx.game.data.Descriptors
import com.mygdx.game.api.Animated
import com.mygdx.game.api.AnimationType
import com.mygdx.game.beans.Position
import com.mygdx.game.data.Assets

class Comics(manager: AssetManager) : Actor(), Animated {
    private val texture = manager.get(Descriptors.comics)
    private val environmentTexture = manager.get(Descriptors.environment)
    private val blackRegion = environmentTexture.findRegion(Assets.EnvironmentAtlas.BLACK)

    private var startFirstBlock = true
    private var startSecondBlock = false

    private var backgroundPos: Position

    init {
        val screenWidth = Gdx.graphics.width
        val textureWidth = texture.width.toFloat()

        backgroundPos = Position((screenWidth - textureWidth) / 2f, 0f, textureWidth, texture.height.toFloat())
        touchable = Touchable.disabled
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(texture, backgroundPos.x, backgroundPos.y, backgroundPos.width, backgroundPos.height)
        drawBlackScene(batch)
    }

    private fun drawBlackScene(batch: Batch) {
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        if (startFirstBlock) {
            //batch.draw(blackRegion, 0f, screenHeight / 2, screenWidth, screenHeight / 2)
            batch.draw(blackRegion, x, y, screenWidth, screenHeight / 2)
        }

        if(!startSecondBlock){
            batch.draw(blackRegion, x, y, screenWidth, screenHeight)
            //batch.draw(blackRegion, 0f, 0f, screenWidth, screenHeight)
        }
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = SHADOW_ANIMATION_TIME_S
        val action = when (type) {
            AnimationType.HIDE_FROM_SCENE -> Actions.alpha(1f, animDuration)
            AnimationType.SHOW_ON_SCENE -> {
                Actions.moveTo(0f, animDuration)

            }
            else -> return
        }
        val run = Actions.run(runAfter)
        val sequence = Actions.sequence(action, run)
        addAction(sequence)
    }
}