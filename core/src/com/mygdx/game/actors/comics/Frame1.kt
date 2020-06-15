package com.mygdx.game.actors.comics

import com.badlogic.gdx.assets.AssetManager
import com.mygdx.game.api.GameActor

class Frame1(manager: AssetManager, private val background: GameActor) : Frame(manager) {

    override fun getFinalMoveX(): Float = background.x + framePadding
    override fun getFinalMoveY(): Float = background.top - (height * finalScale) - framePadding

}