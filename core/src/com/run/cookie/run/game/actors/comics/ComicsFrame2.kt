/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.run.cookie.run.game.data.Assets

class ComicsFrame2(manager: AssetManager, private val comicsFrame1: ComicsFrame) : ComicsFrame(manager) {

    init {
        region = atlas.findRegion(Assets.ComicsAtlas.FRAME_2)
    }

    override fun getFinalMoveX(): Float{
        return comicsFrame1.getFinalMoveX() + (comicsFrame1.width * finalScale) + framePadding
    }
    override fun getFinalMoveY() = comicsFrame1.getFinalMoveY()

}