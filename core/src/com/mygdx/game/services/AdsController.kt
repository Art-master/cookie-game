package com.mygdx.game.services

interface AdsController: ServicesController {
    fun isWifiConnected(): Boolean
    fun isInterstitialLoaded(): Boolean
    fun showBannerAd()
    fun hideBannerAd()
    fun showInterstitialAd(then: Runnable? = null)
    fun showVideoAd(then: Runnable? = null)
}