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
    var limitDistance = minDistance + 700

    enum class Item(val index: Int = 1) {
        SWEETS_BOX,
        ICE_CREAM_BOX(SWEETS_BOX.index + 1),
        CANDY_BOX(ICE_CREAM_BOX.index + 1),
        BABY_COOKIES_BOX(CANDY_BOX.index + 1),
        MILK_BOX(BABY_COOKIES_BOX.index + 1),
        YOGURT_BOX(MILK_BOX.index + 1),
        TOMATO(YOGURT_BOX.index + 1),
        APPLE(TOMATO.index + 1),
        LIME(APPLE.index + 1),
        ORANGE(LIME.index + 1),
        JAM(ORANGE.index + 1),
        JAM2(JAM.index + 1),
        JAM3(JAM2.index + 1),
        JAM4(JAM3.index + 1),
        JAM5(JAM4.index + 1),
        JELLY(JAM5.index + 1),
        JAR_WITH_JAM(JELLY.index + 1),
        JAR_WITH_JAM_2(JAR_WITH_JAM.index + 1),
        ICE_PUDDLE(JAR_WITH_JAM_2.index + 1),
        PUSHPIN(ICE_PUDDLE.index + 1),
        DUNE_BOX(PUSHPIN.index + 1),
        WORMITASH_BOX(DUNE_BOX.index + 1),
    }

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
        actionItems[0].allowUpdate = true
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