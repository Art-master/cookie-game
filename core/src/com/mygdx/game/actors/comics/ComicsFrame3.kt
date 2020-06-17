package com.mygdx.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.mygdx.game.data.Assets

class ComicsFrame3(manager: AssetManager, private val comicsFrame1: ComicsFrame) : ComicsFrame(manager) {

    init {
        region = atlas.findRegion(Assets.ComicsAtlas.FRAME_3)
    }

    override fun getFinalMoveX() = comicsFrame1.getFinalMoveX()

    override fun getFinalMoveY() = comicsFrame1.y - (height * finalScale) - framePadding

}