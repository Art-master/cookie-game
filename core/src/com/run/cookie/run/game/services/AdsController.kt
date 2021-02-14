/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.services

interface AdsController: ServicesController {
    fun isNetworkAvailable(): Boolean
    fun isInterstitialLoaded(): Boolean
    fun showBannerAd()
    fun hideBannerAd()
    fun showInterstitialAd(then: AdsCallback)
    fun showVideoAd(then: AdsCallback)
}

interface AdsCallback {
    fun close()
    fun click()
    fun fail()
}