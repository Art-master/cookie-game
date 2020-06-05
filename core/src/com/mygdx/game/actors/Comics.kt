package com.mygdx.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.data.Descriptors
import com.mygdx.game.api.Animated
import com.mygdx.game.api.AnimationType
import com.mygdx.game.api.GameActor
import com.mygdx.game.beans.Position
import com.mygdx.game.data.Assets

class Comics(manager: AssetManager) : GameActor(), Animated {
    private val texture = manager.get(Descriptors.comics)
    private val environmentTexture = manager.get(Descriptors.environment)
    private val blackRegion = environmentTexture.findRegion(Assets.EnvironmentAtlas.BLACK)

    private var startFirstBlock = true
    private var startSecondBlock = startFirstBlock.not()

    private var backgroundPos: Position

    init {
        val screenWidth = Gdx.graphics.width
        val textureWidth = texture.width.toFloat()
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

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
        if (startFirstBlock) batch.draw(blackRegion, x, y, screenWidth, screenHeight)
        if (startSecondBlock) {
            batch.draw(blackRegion, x, y, screenWidth, screenHeight / 1.98f)
        } else {
            batch.draw(blackRegion, 0f, 0f, screenWidth, screenHeight / 1.98f)
        }
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = 0.5f
        val action = when (type) {
            AnimationType.HIDE_FROM_SCENE -> Actions.alpha(1f, animDuration)
            AnimationType.SHOW_ON_SCENE -> {
                val width = Gdx.graphics.width.toFloat()
                val moveSequence = Actions.sequence(
                        Actions.moveTo(width / 2, y, animDuration),
                        Actions.delay(3f),
                        Actions.moveTo(width, y, animDuration),
                        Actions.delay(3f))
                Actions.sequence(
                        moveSequence,
                        Actions.run { startFirstBlock = false; startSecondBlock = true; x = 0f },
                        Actions.repeat(2, moveSequence)
                )
            }
            else -> return
        }
        val run = Actions.run(runAfter)
        val sequence = Actions.sequence(action, run)
        addAction(sequence)
    }
}