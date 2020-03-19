package com.mygdx.game.data

class Assets {
    class BackgroundTexture{
        companion object{
            const val NAME = "background.jpg"
        }
    }

    class CookieAtlas{
        companion object{
            const val NAME = "cookie.atlas"
            const val RUN = "run"
            const val JUMP_UP = "jump_up"
            const val JUMP_DOWN = "jump_down"
        }
    }

    class EnvironmentAtlas{
        companion object{
            const val NAME = "environment.atlas"
            const val WINDOW = "window"
            const val CURTAIN_RIGHT = "curtainRight"
            const val CURTAIN_LEFT = "curtainLeft"
            const val BLUE_SKY = "blueSky"
            const val MOON = "moon"
            const val FLOWER_IN_POT = "flowerInPot"
            const val FLOWER_IN_POT_2 = "flowerInPot2"
            const val CACTUS = "cactus"
            const val CITY = "city"
            const val CUP = "cup"
            const val JAR1 = "jar1"
            const val JAR2 = "jar2"
            const val JAR3 = "jar3"
            const val CUPBOARD = "cupboard"
            const val OPEN_DOOR = "openDoor"
            const val CLOSE_DOOR = "closeDoor"
            const val PAPER = "paper"
            const val SALT = "salt"
            const val TABLE = "table"
            const val SHADOW = "shadow"
            const val GLASS = "glass"
            const val ORANGE = "orange"
            const val LIME = "lime"
            const val APPLE = "apple"
            const val CARROT = "carrot"
            const val PIE = "pie"
            const val MILK_BOX = "milk_box"
            const val YOGURT_BOX = "yogurt_box"
            const val HAND = "hand"
        }
    }

    class MainMenuAtlas{
        companion object{
            const val NAME = "mainMenu.atlas"
            const val SOUND_ON = "soundOn"
            const val SOUND_OFF = "soundOff"
            const val VIBRATION_ON = "vibrOn"
            const val VIBRATION_OFF = "vibrOff"
            const val BLUR = "blur"
            const val BUTTON_PLAY = "buttonPlay"
            const val BUTTON_PLAY_MINI = "cookieButtonPlayMini"
            const val CIRCLE = "circle"
            const val COOKIE_BUTTON = "cookieButton"
            const val COOKIE_BUTTON_2 = "cookieButton2"
            const val GAME_OVER_TEXT = "gameOverText"
            const val MAIN_MENU_BUTTON = "mainMenuButton"
            const val REPLAY_BUTTON = "replayButton"
            const val TITLE = "title"
        }
    }

    class Fonts{
        companion object{
            const val NAME = "fonts/Arkipelago.ttf"
        }
    }
}