package com.mygdx.game.data

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

object AssetLoader {
    private val manager = AssetManager()
    lateinit var backgroundTexture: Texture
    lateinit var runAnimation: Animation<TextureRegion>

    private lateinit var cookieAtlas: TextureAtlas
    lateinit var environmentAtlas: TextureAtlas

    lateinit var run1: TextureAtlas.AtlasRegion
    lateinit var jumpUp: TextureAtlas.AtlasRegion
    lateinit var jumpDown: TextureAtlas.AtlasRegion
    lateinit var window: TextureAtlas.AtlasRegion
    lateinit var curtainRight: TextureAtlas.AtlasRegion
    lateinit var curtainLeft: TextureAtlas.AtlasRegion
    lateinit var blueSky: TextureAtlas.AtlasRegion
    lateinit var moon: TextureAtlas.AtlasRegion
    lateinit var flower: TextureAtlas.AtlasRegion
    lateinit var flower2: TextureAtlas.AtlasRegion
    lateinit var cactus: TextureAtlas.AtlasRegion
    lateinit var city: TextureAtlas.AtlasRegion
    lateinit var cup: TextureAtlas.AtlasRegion
    lateinit var jar1: TextureAtlas.AtlasRegion
    lateinit var jar2: TextureAtlas.AtlasRegion
    lateinit var jar3: TextureAtlas.AtlasRegion
    lateinit var cupboard: TextureAtlas.AtlasRegion
    lateinit var openDoor: TextureAtlas.AtlasRegion
    lateinit var closeDoor: TextureAtlas.AtlasRegion
    lateinit var paper: TextureAtlas.AtlasRegion
    lateinit var salt: TextureAtlas.AtlasRegion
    lateinit var table: TextureAtlas.AtlasRegion
    lateinit var shadow: TextureAtlas.AtlasRegion
    lateinit var glass: TextureAtlas.AtlasRegion
    lateinit var orange: TextureAtlas.AtlasRegion
    lateinit var lime: TextureAtlas.AtlasRegion
    lateinit var apple: TextureAtlas.AtlasRegion
    lateinit var carrot: TextureAtlas.AtlasRegion
    lateinit var pie: TextureAtlas.AtlasRegion
    lateinit var milk_box: TextureAtlas.AtlasRegion
    lateinit var yogurt_box: TextureAtlas.AtlasRegion

    lateinit var font: FreeTypeFontGenerator

    fun load(){
        backgroundTexture = Texture("background.jpg")
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)

        cookieAtlas = TextureAtlas(Gdx.files.internal("cookie.atlas"))


        run1 = cookieAtlas.findRegion("run")
        jumpUp = cookieAtlas.findRegion("jump_up")
        jumpDown = cookieAtlas.findRegion("jump_down")

        environmentAtlas = TextureAtlas(Gdx.files.internal("environment.atlas"))
        window = environmentAtlas.findRegion("window")
        curtainRight = environmentAtlas.findRegion("curtainRight")
        curtainLeft = environmentAtlas.findRegion("curtainLeft")
        blueSky = environmentAtlas.findRegion("blueSky")
        moon = environmentAtlas.findRegion("moon")
        flower = environmentAtlas.findRegion("flowerInPot")
        flower2 = environmentAtlas.findRegion("flowerInPot2")
        cactus = environmentAtlas.findRegion("cactus")
        city = environmentAtlas.findRegion("city")
        cup = environmentAtlas.findRegion("cup")
        jar1 = environmentAtlas.findRegion("jar1")
        jar2 = environmentAtlas.findRegion("jar2")
        jar3 = environmentAtlas.findRegion("jar3")
        cupboard = environmentAtlas.findRegion("cupboard")
        openDoor = environmentAtlas.findRegion("openDoor")
        closeDoor = environmentAtlas.findRegion("closeDoor")
        paper = environmentAtlas.findRegion("paper")
        salt = environmentAtlas.findRegion("salt")
        table = environmentAtlas.findRegion("table")
        shadow = environmentAtlas.findRegion("shadow")
        glass = environmentAtlas.findRegion("glass")
        orange = environmentAtlas.findRegion("orange")
        lime = environmentAtlas.findRegion("lime")
        apple = environmentAtlas.findRegion("apple")
        carrot = environmentAtlas.findRegion("carrot")
        pie = environmentAtlas.findRegion("pie")
        milk_box = environmentAtlas.findRegion("milk_box")
        yogurt_box = environmentAtlas.findRegion("yogurt_box")

        font = FreeTypeFontGenerator(Gdx.files.internal("fonts/Arkipelago.ttf"))

        runAnimation = Animation(0.1f, cookieAtlas.findRegions("run"), Animation.PlayMode.LOOP)
    }

    fun dispose(){
        backgroundTexture.dispose()
        cookieAtlas.dispose()
        font.dispose()
    }
}