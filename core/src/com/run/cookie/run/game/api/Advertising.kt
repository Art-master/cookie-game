/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

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

    data class Adv(var type: AdType = AdType.NONE,
                   var timeMs: Long = System.currentTimeMillis(),
                   var countOneByOne: Int = 0)
}

