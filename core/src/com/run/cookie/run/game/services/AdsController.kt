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