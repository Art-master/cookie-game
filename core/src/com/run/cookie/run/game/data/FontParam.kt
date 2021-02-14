/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.data

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.graphics.Texture.TextureFilter

enum class FontParam(val fontName: String, private val apply: FreetypeFontLoader.FreeTypeFontLoaderParameter) {
    SCORE("scoreFont.ttf", FreetypeFontLoader.FreeTypeFontLoaderParameter().apply {
        fontFileName = Assets.Fonts.FONT
        fontParameters.color = Color.YELLOW
        fontParameters.shadowOffsetX = -5
        fontParameters.shadowOffsetY = 5
        fontParameters.spaceX = 15
        fontParameters.size = 100
        fontParameters.genMipMaps = true
        fontParameters.minFilter  = TextureFilter.MipMap
        fontParameters.magFilter  = TextureFilter.MipMap
    }),
    CURRENT_SCORE("currentScoreFont.ttf", FreetypeFontLoader.FreeTypeFontLoaderParameter().apply {
        fontFileName = Assets.Fonts.FONT
        fontParameters.color = Color.valueOf("#FFCA28")//yellow
        fontParameters.shadowOffsetX = -5
        fontParameters.shadowOffsetY = 5
        fontParameters.spaceX = 15
        fontParameters.size = 120
        fontParameters.genMipMaps = true
        fontParameters.minFilter  = TextureFilter.MipMap
        fontParameters.magFilter  = TextureFilter.MipMap
    }),
    BEST_SCORE("bestScoreFont.ttf", FreetypeFontLoader.FreeTypeFontLoaderParameter().apply {
        fontFileName = Assets.Fonts.FONT
        fontParameters.color = Color.valueOf("#4CAF50")//green
        fontParameters.shadowOffsetX = -5
        fontParameters.shadowOffsetY = 5
        fontParameters.spaceX = 15
        fontParameters.size = 120
        fontParameters.genMipMaps = true
        fontParameters.minFilter  = com.badlogic.gdx.graphics.Texture.TextureFilter.MipMap
        fontParameters.magFilter  = com.badlogic.gdx.graphics.Texture.TextureFilter.MipMap
    });

    fun get(): FreetypeFontLoader.FreeTypeFontLoaderParameter {
        return apply
    }
}