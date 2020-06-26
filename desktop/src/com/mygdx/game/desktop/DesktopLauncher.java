package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.GdxGame;
import com.mygdx.game.ads.AdsController;

import org.jetbrains.annotations.Nullable;

public class DesktopLauncher{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Run Cookie run";
		config.width = 1920;
		config.height = 1080;

		AdsController controller = new AdsController() {
			@Override
			public boolean isWifiConnected() {
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
			public void showInterstitialAd(@Nullable Runnable then) {

			}
		};
		new LwjglApplication(new GdxGame(controller), config);
	}
}
