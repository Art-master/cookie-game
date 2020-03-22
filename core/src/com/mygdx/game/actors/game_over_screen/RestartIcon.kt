package com.mygdx.game.actors.game_over_screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction
import com.mygdx.game.Config
import com.mygdx.game.actors.Animated
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors

class RestartIcon(manager : AssetManager) : Actor(), Animated {

    private val texture = manager.get(Descriptors.menu)
    private val region = texture.findRegion(Assets.MainMenuAtlas.COOKIE_BUTTON)
    private val replayButton = texture.findRegion(Assets.MainMenuAtlas.REPLAY_BUTTON)
    private val buttonPlay = texture.findRegion(Assets.MainMenuAtlas.BUTTON_PLAY_MINI)

    private var centerX = 0f
    private var centerY = 0f

    init {
        x = 200f
        y = -Gdx.graphics.height.toFloat()
        width = region.originalWidth.toFloat()
        height = region.originalHeight.toFloat()
        setOrigin(replayButton.originalWidth/2f, replayButton.originalHeight/2f )
        addRotateAnimation()
    }

    private  fun addRotateAnimation(){
        addAction(Actions.parallel(Actions.repeat(RepeatAction.FOREVER,
                Actions.sequence(
                        Actions.rotateBy(-360f, 10f),
                        Actions.rotateTo(0f)))))
    }

    override fun act(delta: Float) {
        super.act(delta)

        centerX = x + (width / 2)
        centerY = y + (height / 2)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(region, x, y, width, height)

        drawReplayIcon(batch)
        drawPlayIcon(batch)

    }

    private fun drawReplayIcon(batch: Batch){
        val iconWidth = replayButton.originalWidth.toFloat()
        val iconHeight = replayButton.originalHeight.toFloat()
        val x = centerX - (iconWidth / 2)
        val y = centerY - (iconHeight / 2)

        batch.draw(replayButton, x, y, originX, originY, iconWidth, iconHeight,1f,1f, rotation)
    }

    private fun drawPlayIcon(batch: Batch){
        val iconWidth = buttonPlay.originalWidth.toFloat()
        val iconHeight = buttonPlay.originalHeight.toFloat()
        val x = centerX - (iconWidth / 2)
        val y = centerY - (iconHeight / 2)

        batch.draw(buttonPlay, x, y, iconWidth, iconHeight)
    }

    override fun animate(isReverse:Boolean, runAfter: Runnable) {
        val animDuration = Config.SHADOW_ANIMATION_TIME_S
        val move = if(isReverse){
            Actions.moveTo(x, -Gdx.graphics.height.toFloat(), animDuration, Interpolation.exp10)
        }else{
            val y = 100f
            Actions.moveTo(x, y, animDuration)
        }
        val run = Actions.run(runAfter)
        val sequence = Actions.sequence(move, run)
        addAction(sequence)
    }
}