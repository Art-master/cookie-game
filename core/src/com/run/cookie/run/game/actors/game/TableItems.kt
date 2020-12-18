package com.run.cookie.run.game.actors.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Array
import com.run.cookie.run.game.actors.game.cookie.Cookie
import com.run.cookie.run.game.api.Callback
import kotlin.random.Random

class TableItems(private val manager: AssetManager,
                 private val table: Table,
                 private val cookie: Cookie) {

    private val minDistance = 200
    private val random = Random(minDistance)
    private val randomItemNum = 5
    private val limitDistance = minDistance + 300

    private val actionItems = Array<RandomTableItem>(randomItemNum)
    var isStopGenerate = false
        set(value) {
            field = value
            if (value) getActors().forEach { it.isStopGeneration = true }
        }

    init {
        initTableItems()
        startFirst()
    }

    /**
     * Init items on the table and control its distance
     */
    private fun initTableItems() {
        for (i in 0 until randomItemNum) {
            val actor = RandomTableItem(manager, table, cookie)
            actor.distanceUntil = random.nextInt(minDistance, limitDistance)
            if (i > 0) actor.prevActor = actionItems.get(i - 1)
            actor.callback = object : Callback {
                override fun call() {
                    if (isStopGenerate) return
                    actor.distanceUntil = random.nextInt(minDistance, limitDistance)
                }

            }
            actionItems.add(actor)
        }
        actionItems.apply {
            this[0].prevActor = this[size - 1]
        }
    }

    private fun startFirst() {
        actionItems[0].startAct = true
    }

    fun isAllObjectLeft(): Boolean {
        getActors().forEach {
            if (it.isItemLeft().not()) return false
        }
        return true
    }

    fun getInvolvedObjects(): Int {
        var countLeft = 0
        getActors().forEach {
            if (it.isItemLeft()) countLeft++
        }
        return getActors().size - countLeft
    }

    fun isAllObjectsScored(): Boolean {
        var counter = 0
        getActors().forEach {
            if (it.isScored.not()) return@forEach
            counter++
        }
        return counter == getActors().size
    }

    fun getActors() = actionItems
}