package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.Config
import com.mygdx.game.actors.SoundIcon
import com.mygdx.game.actors.VibrationIcon
import com.mygdx.game.data.Descriptors

class StartScreen : Screen {
    private val manager = AssetManager()
    private val camera = OrthographicCamera(Config.widthGame, Config.heightGame)
    private val stage = Stage(ScreenViewport(camera))

    private val table = Table()

    init {
        loadResources()
        Gdx.input.inputProcessor = stage
    }

    private fun loadResources(){
        manager.load(Descriptors.background)
        manager.load(Descriptors.menu)
        manager.load(Descriptors.environment)
        manager.finishLoading()
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if(manager.isFinished && stage.actors.isEmpty){
            //drawTable()
            val soundIcon = SoundIcon(manager)
            val vibrationIcon = VibrationIcon(manager, soundIcon)

            stage.addActor(soundIcon)
            stage.addActor(vibrationIcon)
        }
        stage.act(delta)
        stage.draw()
    }

    private fun drawTable(){
        table.apply {
            //columnDefaults(2)
            row().width(300f).height(300f)
            add(SoundIcon(manager))
            //add(VibrationIcon(manager))
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
    }
}