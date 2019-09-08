package com.mygdx.game.render

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.mygdx.game.actors.*
import com.mygdx.game.world.GameWorld

class GameRender {
    private val stage = Stage(ScreenViewport())

    private val background = Background()
    private val table = Table()
    private val window = Window()
    private val moon = Moon(window)
    private val city = City(window)
    private val sky = Sky(window)
    private val flower = FlowerInPot(window)
    private val cookie = Cookie()
    private val shadow = Shadow()
    private val cupboard = Cupboard(window)
    private val score = Score()

    private val gameWorld = GameWorld(cookie, background, city, cupboard, flower, moon, score, shadow, sky, table, window)

    init {
        Gdx.input.inputProcessor = stage
        setListeners()

    }


    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    fun render(delta: Float, runTime: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)

        stage.addActor(background)
        stage.addActor(cupboard)
        stage.addActor(shadow)
        stage.addActor(sky)
        stage.addActor(moon)
        stage.addActor(city)
        stage.addActor(window)
        stage.addActor(flower)



        stage.addActor(table)
        stage.addActor(cookie)
        stage.addActor(score)
        stage.draw()
    }

    private fun setListeners(){
        stage.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                cookie.onClick()
            }
        })
    }

    fun dispose() {
        stage.dispose()
    }

}