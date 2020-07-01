package com.mygdx.game;

import org.jetbrains.annotations.Nullable;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.mygdx.game.ads.AdsController;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new GdxGame(new AdsController() {
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

            @Override
            public void showVideoAd(@Nullable Runnable then) {

            }
        }), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}