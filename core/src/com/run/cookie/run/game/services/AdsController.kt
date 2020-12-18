package com.run.cookie.run.game.services

interface AdsController: ServicesController {
    fun isNetworkAvailable(): Boolean
    fun isInterstitialLoaded(): Boolean
    fun showBannerAd()
    fun hideBannerAd()
    fun showInterstitialAd(then: Runnable? = null)
    fun showVideoAd(then: Runnable? = null)
}