package com.run.cookie.run.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.run.cookie.run.game.services.AdsCallback;
import com.run.cookie.run.game.services.AdsController;
import com.run.cookie.run.game.services.CallBack;
import com.run.cookie.run.game.services.ServicesController;

import org.jetbrains.annotations.NotNull;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.vSyncEnabled = true;
        config.title = "Run, Cookie, run";
        config.width = 1920;
        config.height = 1080;

        AdsController controller = new Controller();
        new LwjglApplication(new GdxGame(controller), config);
    }

    static class Controller implements AdsController, ServicesController {

        @Override
        public void unlockAchievement(@NotNull Config.Achievement achievement) {

        }

        @Override
        public void incrementAchievement(@NotNull Config.Achievement achievement, int value) {

        }

        @Override
        public void showAllAchievements() {

        }

        @Override
        public boolean isNetworkAvailable() {
            return false;
        }

        @Override
        public boolean isInterstitialLoaded() {
            return false;
        }

        @Override
        public void showBannerAd() {

        }

        @Override
        public void hideBannerAd() {

        }

        @Override
        public void signIn() {

        }

        @Override
        public void signInSilently() {

        }

        @Override
        public void signOut() {

        }

        @Override
        public void submitScore(long highScore) {

        }

        @Override
        public void showLeaderboard() {

        }

        @Override
        public void getPlayerCenteredScores(@NotNull CallBack callBack) {

        }

        @Override
        public void getTopScores(int scoreType, @NotNull CallBack callBack) {

        }

        @Override
        public boolean isSignedIn() {
            return false;
        }

        @Override
        public void share(int score) {

        }

        @Override
        public void showInterstitialAd(@NotNull AdsCallback then) {

        }

        @Override
        public void showVideoAd(@NotNull AdsCallback then) {

        }
    }
}
