package com.mygdx.game.services

import java.lang.Exception

interface LeaderboardController {
    fun signIn()
    fun signOut()
    fun submitScore(highScore: Long)
    fun showLeaderboard()
    fun getPlayerCenteredScores(callBack: CallBack)
    fun getTopScores(scoreType: Int, callBack: CallBack)
    fun isSignedIn(): Boolean
}

interface CallBack{
    fun success(list: List<String>)
    fun fail(exception: Exception, string: String)
}