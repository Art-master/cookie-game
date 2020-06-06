package com.mygdx.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.mygdx.game.data.Assets

class Frame3(manager: AssetManager, private val frame1: Frame) : Frame(manager) {

    init {
        region = atlas.findRegion(Assets.ComicsAtlas.FRAME_3)
    }

    override fun getFinalMoveX() = frame1.getFinalMoveX()

    override fun getFinalMoveY() = 30f

}