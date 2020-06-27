package com.mygdx.game.actors.loading_progress

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.badlogic.gdx.graphics.Color
import com.mygdx.game.Config
import com.mygdx.game.api.GameActor

class ProgressBar(manager : AssetManager) : GameActor() {
    private val progressAtlas = manager.get(Descriptors.progressBar)
    private val barRegion = progressAtlas.findRegion(Assets.ProgressAtlas.PROGRESS_BAR)
    private val progressLineRegion = progressAtlas.findRegion(Assets.ProgressAtlas.PROGRESS_LINE)
    private var progressLineWidth = 0f
    private val progressLineHeight = progressLineRegion.originalHeight.toFloat()

    init {
        width = barRegion.originalWidth.toFloat()
        height = barRegion.originalHeight.toFloat()
        x = (Config.WIDTH_GAME - width) / 2
        y = 200f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = Color.WHITE
        batch.draw(barRegion, x, y, width, height)

        drawProgress(batch)
    }

    private fun drawProgress(batch: Batch){
        batch.draw(progressLineRegion, x + 15, y + 15, progressLineWidth, progressLineHeight)
    }

    fun setProgress(progress: Float) {
        progressLineWidth = if(progress == 0.1f){
            progressLineRegion.originalWidth.toFloat()
        } else (progressLineRegion.originalWidth / 100) * (progress * 100)
    }
}