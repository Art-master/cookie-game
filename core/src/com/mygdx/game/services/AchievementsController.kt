package com.mygdx.game.services

import com.mygdx.game.Config

interface AchievementsController {
    fun unlockAchievement(achievement: Config.Achievement)
    fun incrementAchievement(achievement: Config.Achievement, value: Int)
    fun showAllAchievements()
}