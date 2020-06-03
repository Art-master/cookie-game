package com.mygdx.game.data

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class Descriptors {
    companion object{
        val background = AssetDescriptor<Texture>(Assets.BackgroundTexture.NAME, Texture::class.java)
        val comics = AssetDescriptor<Texture>(Assets.ComicsTexture.NAME, Texture::class.java)
        val cookie = AssetDescriptor<TextureAtlas>(Assets.CookieAtlas.NAME, TextureAtlas::class.java)
        val environment = AssetDescriptor<TextureAtlas>(Assets.EnvironmentAtlas.NAME, TextureAtlas::class.java)
        val menu = AssetDescriptor<TextureAtlas>(Assets.MainMenuAtlas.NAME, TextureAtlas::class.java)
    }
}