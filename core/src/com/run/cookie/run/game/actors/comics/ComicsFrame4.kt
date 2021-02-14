/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.run.cookie.run.game.data.Assets

class ComicsFrame4(manager: AssetManager, private val comicsFrame2: ComicsFrame, private val comicsFrame3: ComicsFrame) : ComicsFrame(manager) {

    init {
        region = atlas.findRegion(Assets.ComicsAtlas.FRAME_4)
    }

    override fun getFinalMoveX() = comicsFrame2.getFinalMoveX()

    override fun getFinalMoveY() = comicsFrame3.getFinalMoveY()

}