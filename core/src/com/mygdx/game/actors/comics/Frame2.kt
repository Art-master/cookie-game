package com.mygdx.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.mygdx.game.data.Assets

class Frame2(manager: AssetManager, private val frame1: Frame) : Frame(manager) {

    init {
        region = atlas.findRegion(Assets.ComicsAtlas.FRAME_2)
    }

    override fun getFinalMoveX(): Float{
        return frame1.getFinalMoveX() + (frame1.width * finalScale) + framePadding
    }
    override fun getFinalMoveY() = frame1.getFinalMoveY()

}