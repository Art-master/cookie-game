package com.mygdx.game.data

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader

class Parameters {
    companion object{
        val scoreFontParams = FreetypeFontLoader.FreeTypeFontLoaderParameter().apply {
            fontFileName = Assets.Fonts.SCORE_FONT
            fontParameters.color = Color.YELLOW
            fontParameters.shadowOffsetX = -5
            fontParameters.shadowOffsetY = 5
            fontParameters.size = 100
        }
    }
}