package com.mygdx.game.data

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class Descriptors {
    companion object{
        val background = AssetDescriptor(Assets.BackgroundTexture.NAME, Texture::class.java)
        val gameOverBackground = AssetDescriptor(Assets.GameOverBackground.NAME, Texture::class.java)
        val comics = AssetDescriptor(Assets.ComicsAtlas.NAME, TextureAtlas::class.java)
        val cookie = AssetDescriptor(Assets.CookieAtlas.NAME, TextureAtlas::class.java)
        val environment = AssetDescriptor(Assets.EnvironmentAtlas.NAME, TextureAtlas::class.java)
        val menu = AssetDescriptor(Assets.MainMenuAtlas.NAME, TextureAtlas::class.java)
    }
}