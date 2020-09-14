package com.mygdx.game.actors.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.mygdx.game.api.GameActor
import com.mygdx.game.data.Assets
import com.mygdx.game.data.Descriptors
import com.mygdx.game.api.Listener
import java.util.*

class FlowerInPot(manager : AssetManager, private val window: Window) : GameActor(){
    private val texture = manager.get(Descriptors.environment)
    private var flowerTexture = texture.findRegion(Assets.EnvironmentAtlas.FLOWER_IN_POT)

    private var isShowFlower = true
    private var tab = 40
    private val randShowing = Random(tab.toLong())
    private val randFlowerType = Random()
    private val numObjects = 3
    init {
        window.addResetListener(object : Listener {
            override fun call() {
                val random = Random().nextInt(2)
                isShowFlower = random == 0
                tab = randShowing.nextInt(700)
                flowerTexture = getFlowerType(randFlowerType.nextInt(numObjects))
            }
        })
    }

    private fun getFlowerType(num: Int): TextureAtlas.AtlasRegion{
        return when(num){
            1 -> texture.findRegion(Assets.EnvironmentAtlas.CACTUS)
            2 -> texture.findRegion(Assets.EnvironmentAtlas.FLOWER_IN_POT_2)
            else -> texture.findRegion(Assets.EnvironmentAtlas.FLOWER_IN_POT)
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if(isShowFlower){
            val padding = window.getWindowsillX() + tab
            val width = flowerTexture.originalWidth.toFloat()
            val height = flowerTexture.originalHeight.toFloat()
            batch!!.draw(flowerTexture, padding, window.getWindowsillY(), width, height)
        }
    }
}