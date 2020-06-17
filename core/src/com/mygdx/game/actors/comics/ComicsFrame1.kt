package com.mygdx.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.mygdx.game.Config
import com.mygdx.game.api.GameActor

class ComicsFrame1(manager: AssetManager, private val background: GameActor) : ComicsFrame(manager) {

    override fun getFinalMoveX(): Float = (Config.WIDTH_GAME - ((width * finalScale) * 2) - framePadding) / 2
    override fun getFinalMoveY(): Float = background.top - (height * finalScale) - framePadding
}