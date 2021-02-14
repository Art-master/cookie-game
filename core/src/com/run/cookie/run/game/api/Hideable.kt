/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.api

import com.run.cookie.run.game.beans.Position

class Hideable(private val posHidingObj: Position, private val posHideObj: Position) {

    private val posHideObjX2 = posHideObj.x + posHideObj.width
    private val posHideObjY2 = posHideObj.y + posHideObj.height
    private val posHidingObjX2 = posHidingObj.x + posHidingObj.width
    private val posHidingObjY2 = posHidingObj.y + posHidingObj.height

    fun getX() : Float{
        return if(posHideObj.x < posHidingObj.x){
            posHidingObj.x
        } else posHideObj.x
    }

    fun getY() : Float{
        return if(posHideObj.y < posHidingObj.y){
            posHidingObj.y
        } else posHideObj.y
    }

    fun getTextureX(): Float{
        return if(posHideObj.x > posHidingObj.x && posHidingObjX2 >= posHideObj.x){
            posHidingObj.width - (posHidingObjX2 - posHideObj.x)
        } else 0f
    }

    fun getTextureY(): Float{
        return if(posHideObj.y > posHidingObj.y && posHidingObjY2 >= posHideObj.y){
            posHidingObjY2 - (posHidingObjY2 - posHideObj.y)
        } else 0f
    }

    fun getDrawWidth(): Float{
        val x = getX()
        return if(posHidingObjX2 > posHideObj.x && x <= posHideObjX2){
            posHideObjX2 - x
        } else 0f
    }

    fun getDrawHeight(): Float{
        val y = getY()
        return if(posHidingObjY2 > posHideObj.y && y <= posHideObjY2){
            posHideObjY2 - y
        } else 0f
    }

}