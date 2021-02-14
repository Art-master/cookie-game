/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.GameActor

class ComicsFrame1(manager: AssetManager, private val background: GameActor) : ComicsFrame(manager) {

    override fun getFinalMoveX(): Float = (Config.WIDTH_GAME - ((width * finalScale) * 2) - framePadding) / 2
    override fun getFinalMoveY(): Float = Config.HEIGHT_GAME - (height * finalScale) - framePadding
}