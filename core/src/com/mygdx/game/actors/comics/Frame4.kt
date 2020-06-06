package com.mygdx.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.mygdx.game.data.Assets

class Frame4(manager: AssetManager, private val frame2: Frame, private val frame3: Frame) : Frame(manager) {

    init {
        region = atlas.findRegion(Assets.ComicsAtlas.FRAME_4)
    }

    override fun getFinalMoveX() = frame2.getFinalMoveX()

    override fun getFinalMoveY() = frame3.getFinalMoveY()

}