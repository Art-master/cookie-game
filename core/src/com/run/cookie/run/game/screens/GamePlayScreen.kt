/*
 * Copyright (C) Art-_-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.run.cookie.run.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.run.cookie.run.game.Config
import com.run.cookie.run.game.Config.Debug
import com.run.cookie.run.game.DebugUtils
import com.run.cookie.run.game.actors.Shadow
import com.run.cookie.run.game.actors.game.*
import com.run.cookie.run.game.actors.game.TableItemsManager.Item
import com.run.cookie.run.game.actors.game.cookie.*
import com.run.cookie.run.game.actors.game.cookie.Cookie.State
import com.run.cookie.run.game.api.AnimationType
import com.run.cookie.run.game.api.Callback
import com.run.cookie.run.game.api.Scrollable
import com.run.cookie.run.game.api.WallActor
import com.run.cookie.run.game.data.Assets
import com.run.cookie.run.game.managers.AudioManager
import com.run.cookie.run.game.managers.AudioManager.MusicApp
import com.run.cookie.run.game.managers.AudioManager.SoundApp
import com.run.cookie.run.game.managers.ScreenManager
import com.run.cookie.run.game.managers.VibrationManager
import com.run.cookie.run.game.services.ServicesController
import kotlin.random.Random

class GamePlayScreen(params: Map<ScreenManager.Param, Any>) : GameScreen(params) {
    private val background = Background(manager)
    private val table = Table(manager, 240f)
    private val window = Window(manager, 270f)
    private val city = City(manager, window)
    private val flower = FlowerInPot(manager, window)
    private val cookie = Cookie(manager, table.worktopY, Config.WIDTH_GAME / 2)
    private val jumpDust = JumpDust(manager, cookie)
    private val fallDust = FallDust(manager, cookie)
    private val sunglasses = CookieItem(manager, cookie, Assets.CookieAtlas.SUNGLASSES)
    private val hat = CookieItem(manager, cookie, Assets.CookieAtlas.HAT)
    private val boots = CookieItem(manager, cookie, Assets.CookieAtlas.BOOTS)
    private val belt = CookieItem(manager, cookie, Assets.CookieAtlas.BELT)
    private val gun = CookieItem(manager, cookie, Assets.CookieAtlas.GUN)
    private val bullets = CookieItem(manager, cookie, Assets.CookieAtlas.BULLETS)
    private val shot = Shot(manager, cookie)
    private val cookieShadow = CookieShadow(manager, cookie)
    private val kitchenShadow = com.run.cookie.run.game.actors.game.Shadow(manager)
    private val cupboard = Cupboard(manager, 510f)
    private val score = Score(manager)
    private val arm = Arm(manager, cookie)
    private val items = TableItemsManager(manager, table, cookie)
    val actors: Array<Actor> = Array()
    private val wallActors: Array<WallActor> = Array()

    private var touchable = true
    private var isGameOver = false
    private var isWinGame = false

    init {
        actors.addAll(background, cupboard, kitchenShadow, city, window, flower, table)
        if (!Debug.EMPTY_TABLE.state) actors.addAll(items.getActors())
        actors.addAll(cookieShadow, cookie, jumpDust, sunglasses, hat, boots, belt, gun, bullets, fallDust, arm, score, shot)
        wallActors.addAll(window, cupboard)
        cookie.listeners.addAll(jumpDust, fallDust)

        addActorsToStage()
        stopMoveAllActors()
        startInitAnimation()
        changeScore()
        controlWallActors()

        if (Debug.PERIODIC_JUMP.state) {
            DebugUtils.startPeriodicTimer(1f, 2f) {
                cookie.startJumpForce()
            }
        }


        stage.addListener(object : ClickListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (cookie.state == State.RUN && touchable && isWinGame.not()) {
                    cookie.startJumpForce()
                    AudioManager.play(SoundApp.JUMP)
                    VibrationManager.cancel()
                }
                return super.touchDown(event, x, y, pointer, button)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                AudioManager.stop(SoundApp.JUMP)
                if (touchable && isWinGame.not()) {
                    cookie.endJumpForce()
                }
                super.touchUp(event, x, y, pointer, button)
            }
        }
        )
        AudioManager.play(MusicApp.GAME_MUSIC, true)
        Gdx.input.inputProcessor = stage
    }


    private fun addActorsToStage() {
        for (actor in actors) {
            stage.addActor(actor)
        }
    }

    private fun startInitAnimation() {
        shadow.animate(AnimationType.SHOW_ON_SCENE, Runnable {
            cookie.animate(AnimationType.SHOW_ON_SCENE, Runnable {
                startMoveAllActors()
                (wallActors.first() as Scrollable).runMove()
            })
            arm.animate(AnimationType.SHOW_ON_SCENE, Runnable {
                arm.startRepeatableMove()
            })
        })
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        val bufferBitMv = if (Gdx.graphics.bufferFormat.coverageSampling) GL20.GL_COVERAGE_BUFFER_BIT_NV else 0
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT or bufferBitMv)

        applyStages(delta)
        checkContactCookieAndHand()
        if (items.isStopGenerate) controlWinning()
    }

    private fun changeScore() {
        items.getActors().forEach { controlScore(it) }
    }

    private fun controlWallActors() {
        val random = Random(500)
        for (i in 0 until wallActors.size) {
            val actor = wallActors[i]
            if (i < wallActors.size - 1) actor.nextActor = wallActors.get(i + 1) as Actor
            actor.distancePastListener = {
                (actor.nextActor as WallActor).resetState()
                (actor.nextActor as WallActor).distance = random.nextInt(900, 1200)
            }
        }
        wallActors.apply {
            this.last().nextActor = this.first() as Actor
            this.first().distance = random.nextInt(900, 1200)
        }
    }

    private fun controlScore(actor: TableItem) {
        val controller = ScreenManager.globalParameters[ScreenManager.Param.SERVICES_CONTROLLER] as ServicesController
        actor.callbackGoThrough = object : Callback {
            override fun call() {
                score.scoreNum++
                controlItemsAppearance()
                when (score.scoreNum) {
                    Config.Achievement.SUNGLASSES.score -> {
                        sunglasses.animate(AnimationType.SHOW_ON_SCENE, Runnable {
                            controller.unlockAchievement(Config.Achievement.SUNGLASSES)
                        })
                    }
                    Config.Achievement.HAT.score -> {
                        hat.animate(AnimationType.SHOW_ON_SCENE, Runnable {
                            controller.unlockAchievement(Config.Achievement.HAT)
                        })
                    }
                    Config.Achievement.BOOTS.score -> {
                        boots.animate(AnimationType.SHOW_ON_SCENE, Runnable {
                            controller.unlockAchievement(Config.Achievement.BOOTS)
                        })
                    }
                    Config.Achievement.BELT.score -> {
                        belt.animate(AnimationType.SHOW_ON_SCENE, Runnable {
                            controller.unlockAchievement(Config.Achievement.BELT)
                        })
                    }
                    Config.Achievement.GUN.score -> {
                        gun.animate(AnimationType.SHOW_ON_SCENE, Runnable {
                            controller.unlockAchievement(Config.Achievement.GUN)
                        })
                    }
                    Config.Achievement.BULLETS.score -> {
                        bullets.animate(AnimationType.SHOW_ON_SCENE, Runnable {
                            controller.unlockAchievement(Config.Achievement.BULLETS)
                        })
                    }
                    Config.Achievement.FINISH_GAME.score -> {
                        items.isStopGenerate = true
                    }
                }
            }
        }
    }

    private fun controlItemsAppearance() {
        if (score.scoreNum % 10 == 0) {
            Config.currentScrollSpeed = Config.currentScrollSpeed + Config.SPEED_INCREASE_STEP
            items.limitDistance -= Config.ITEMS_DISTANCE_INCREASE_STEP

            if (Debug.MIN_DISTANCE.state) {
                items.limitDistance = Config.ITEMS_DISTANCE_INCREASE_STEP * (Config.Achievement.FINISH_GAME.score / 10)
            }

            foreEachActor {
                if (it is Scrollable) it.updateSpeed()
            }
        }
        if (score.scoreNum % 20 == 0 && Debug.MAX_RANDOM_ITEMS.state.not()) {
            items.increaseItemsAppearancePercent(Item.PUSHPIN, Item.ICE_PUDDLE, Item.JELLY)
        }
    }


    private fun stopMoveAllActors() {
        for (actor in actors) {
            if (actor is Scrollable && actor !is Cookie) {
                actor.stopMove()
            }
        }
    }

    private fun startMoveAllActors() {
        foreEachActor {
            if (it is Scrollable && it !is WallActor) it.runMove()
        }
    }

    private fun foreEachActor(callbackData: (Actor) -> Unit) {
        for (actor in actors) {
            callbackData.invoke(actor)
        }
    }

    private var cookieReadyForShot = false

    private fun controlWinning() {
        if (items.isAllObjectsScored()) isWinGame = true
        if (items.isAllObjectLeft()) {
            cookie.win()
            arm.isWinningAnimation = true
            stopMoveAllActors()

            if (cookie.x < cookie.startX || cookieReadyForShot) return
            cookieReadyForShot = true

            AudioManager.stopAllMusics()
            actors.filterIsInstance<CookieItem>().forEach {
                (it as Actor).remove()
            }
            shot.animate(AnimationType.SHOW_ON_SCENE, Runnable {
                shadow.invertColor()
                shadow.animate(AnimationType.SHOW_ON_SCENE)

                AudioManager.play(SoundApp.GUN_SHOT)

                VibrationManager.vibrate(VibrationManager.VibrationType.BOOM)
                arm.animate(AnimationType.HIDE_FROM_SCENE, Runnable {
                    val shadow = Shadow(manager)
                    stage.addActor(shadow)
                    arm.remove()
                    shadow.animate(AnimationType.HIDE_FROM_SCENE, Runnable {
                        val params = mapOf(
                                Pair(ScreenManager.Param.SCORE, score.scoreNum),
                                Pair(ScreenManager.Param.WAS_WIN_GAME, true))

                        ScreenManager.setScreen(ScreenManager.Screens.GAME_OVER, params)
                    })
                })
            })
        }
    }

    private fun checkContactCookieAndHand() {
        if (arm.x + arm.width >= cookie.x && isGameOver.not()) {
            isGameOver = true
            touchable = false
            stopMoveAllActors()
            cookie.caught()
            arm.actions.clear()
            arm.isGameOverAnimation = true
            VibrationManager.cancel()
            arm.animate(AnimationType.COOKIE_CATCH, Runnable {
                actors.filterIsInstance<CookieItem>().forEach {
                    it.animate(AnimationType.HIDE_FROM_SCENE)
                    VibrationManager.vibrate(VibrationManager.VibrationType.ACTOR_CATCH)
                }
                arm.animate(AnimationType.HIDE_FROM_SCENE, Runnable {
                    ScreenManager.setScreen(ScreenManager.Screens.GAME_OVER, Pair(ScreenManager.Param.SCORE, score.scoreNum))
                })
            })
            AudioManager.stopAll()
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        Config.currentScrollSpeed = Config.DEFAULT_SCROLL_SPEED
        super.dispose()
    }
}