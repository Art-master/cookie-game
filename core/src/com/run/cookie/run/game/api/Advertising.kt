package com.run.cookie.run.game.api

class Advertising {
    enum class AdType { NONE, BANNER, VIDEO, INTERSTITIAL }

    var commonClickCount = 0

    var last = Adv()
    set(value) {
        field = value
        history.add(value)
        if(history.size > 10) history.removeAt(0)
    }
    var history = ArrayList<Adv>()

    data class Adv(var timeMs: Long = 0, var type: AdType = AdType.NONE, var lastCountOneByOne: Int = 1)
}

