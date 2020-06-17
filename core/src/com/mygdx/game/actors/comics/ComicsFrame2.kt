package com.mygdx.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.mygdx.game.data.Assets

class ComicsFrame2(manager: AssetManager, private val comicsFrame1: ComicsFrame) : ComicsFrame(manager) {

    init {
        region = atlas.findRegion(Assets.ComicsAtlas.FRAME_2)
    }

    override fun getFinalMoveX(): Float{
        return comicsFrame1.getFinalMoveX() + (comicsFrame1.width * finalScale) + framePadding
    }
    override fun getFinalMoveY() = comicsFrame1.getFinalMoveY()

}