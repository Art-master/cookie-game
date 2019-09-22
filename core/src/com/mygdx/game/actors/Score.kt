package com.mygdx.game.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.Assets

class Score(manager : AssetManager) : Actor() {
    private val generator = FreeTypeFontGenerator(Gdx.files.internal(Assets.Fonts.NAME))
    private var score: BitmapFont

    init {
        val offSetShadow = 5

        val param = FreeTypeFontGenerator.FreeTypeFontParameter()
        param.color = Color.YELLOW
        param.shadowOffsetX = -offSetShadow
        param.shadowOffsetY = offSetShadow
        param.size = 100
        score = generator.generateFont(param)

        //shadow = generator.generateFont(param)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val fontX = Gdx.graphics.width -200f
        val fontY = Gdx.graphics.height -50f
        score.draw(batch, "300", fontX, fontY)
        //batch!!.color = Color.GRAY
    }
}