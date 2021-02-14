/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.actors.game.cookie

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.run.cookie.run.game.api.Animated
import com.run.cookie.run.game.api.AnimationType
import com.run.cookie.run.game.api.GameActor
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.data.Descriptors

class Shot(manager : AssetManager, private val cookie: Cookie) : GameActor(), Animated{
    private val texture = manager.get(Descriptors.cookie)
    private var shotRegion = texture.findRegion(Assets.CookieAtlas.BOOM)

    init {
        width = shotRegion.originalWidth.toFloat()
        height = shotRegion.originalHeight.toFloat()
        setScale(0.1f)
        color.a = 0f
    }

    override fun act(delta: Float) {
        super.act(delta)
        if(cookie.state == Cookie.State.WIN){
            x = cookie.x - (width * scaleX) - 50
            y = cookie.y + 50
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if(cookie.state == Cookie.State.WIN) {
            batch!!.setColor(color.r, color.g, color.b, color.a)
            batch.draw(shotRegion, x,  y, 0f, 0f, width, height, scaleX, scaleY, rotation)
        }
    }

    override fun animate(type: AnimationType, runAfter: Runnable) {
        val animDuration = 0.3f
        val action = when(type) {
            AnimationType.SHOW_ON_SCENE -> {
                val act1 = Actions.delay(1f)
                val act2 = Actions.parallel(
                        Actions.scaleTo(1f, 1f, animDuration, Interpolation.linear),
                        Actions.alpha(1f, 0.1f, Interpolation.slowFast)
                )
                Actions.sequence(act1, act2, Actions.run(runAfter))
            }
            else -> return
        }
        addAction(action)
    }
}