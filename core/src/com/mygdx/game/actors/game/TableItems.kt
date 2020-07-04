package com.mygdx.game.actors.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Array
import com.mygdx.game.actors.game.cookie.Cookie
import com.mygdx.game.api.Callback
import kotlin.random.Random

class TableItems(private val manager : AssetManager,
                 private val table : Table,
                 private val cookie: Cookie) {

    private val minDistance = 500
    private val random = Random(minDistance)
    private val randomItemNum = 5
    private val limitDistance = minDistance + 50
    private val actionItems = Array<RandomTableItem>(randomItemNum)

    init {
        initTableItems()
        startFirst()
    }

    private fun initTableItems(){
        for(i in 0 until randomItemNum){
            val actor = RandomTableItem(manager, table, cookie)
            actor.distanceUntil = random.nextInt(minDistance, limitDistance)
            if(i > 0)actor.prevActor = actionItems.get(i - 1)
            actor.callback = object : Callback{
                override fun call() {
                    actor.distanceUntil = random.nextInt(minDistance, limitDistance)
                }

            }
            actionItems.add(actor)
        }
        actionItems.apply {
            this[0].prevActor = this[size -1]
        }
    }

    private fun startFirst(){
        actionItems[0].startAct = true
    }

    fun act(delta: Float){

    }

    fun getActors() = actionItems
}