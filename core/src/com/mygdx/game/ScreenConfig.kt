package com.mygdx.game

import com.badlogic.gdx.Gdx


object ScreenConfig {
    const val widthGame = 1920f
    const val heightGame = 1080f

    private val heightDevise = Gdx.graphics.height.toFloat()
    private val widthDevise = Gdx.graphics.width.toFloat()

    var R: Float = 0f

    val zoom: Float
        get() {
            if (Gdx.graphics.height < Gdx.graphics.width) {
                when {
                    heightGame > Gdx.graphics.height -> R = heightDevise / heightGame
                    heightGame < Gdx.graphics.height -> R = heightGame / heightDevise
                    heightGame == heightDevise -> R = 1f
                }
            } else {
                when {
                    widthGame > Gdx.graphics.width -> R = widthDevise / widthGame
                    widthGame < Gdx.graphics.width -> R = widthGame / widthDevise
                    widthGame == widthDevise -> R = 1f
                }
            }

            return R
        }
}