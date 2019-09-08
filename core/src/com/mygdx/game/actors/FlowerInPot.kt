package com.mygdx.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.data.AssetLoader
import com.mygdx.game.impl.Listener
import java.util.*

class FlowerInPot(private val window: Window) : Actor(){
    private var flowerTexture = AssetLoader.flower
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
            1 -> AssetLoader.cactus
            2 -> AssetLoader.flower2
            else -> AssetLoader.flower
        }
    }
    override fun act(delta: Float) {
        super.act(delta)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if(isShowFlower){
            batch!!.draw(flowerTexture, window.getWindowsillX() + tab, window.getWindowsillY(),
                    flowerTexture.originalWidth.toFloat(), flowerTexture.originalHeight.toFloat())
        }
    }
}