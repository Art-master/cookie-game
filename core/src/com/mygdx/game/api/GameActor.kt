package com.mygdx.game.api

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.mygdx.game.managers.VibrationManager

open class GameActor : Actor() {

    val tailX: Float
        get() = x + width

    val tailY: Float
        get() = y + height

    var isVibrating = false
    set(value) {
        field = value
        if(value){
            addListener(vibrationListener)
        }else{
            removeListener(vibrationListener)
        }
    }

    private val vibrationListener = object: ClickListener(){
        override fun clicked(event: InputEvent?, x: Float, y: Float) {
            super.clicked(event, x, y)
            if(isVibrating){
                VibrationManager.vibrate()
            }
        }
    }
}