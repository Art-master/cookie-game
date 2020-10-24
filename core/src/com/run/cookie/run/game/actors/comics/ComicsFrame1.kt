package com.run.cookie.run.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.api.GameActor

class ComicsFrame1(manager: AssetManager, private val background: GameActor) : ComicsFrame(manager) {

    override fun getFinalMoveX(): Float = (Config.WIDTH_GAME - ((width * finalScale) * 2) - framePadding) / 2
    override fun getFinalMoveY(): Float = background.top - (height * finalScale) - framePadding
}