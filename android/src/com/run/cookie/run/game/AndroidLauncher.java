package com.run.cookie.run.game;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AnnotatedData;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.run.cookie.run.game.services.CallBack;
import com.run.cookie.run.game.services.ServicesController;
import com.run.cookie.run.game.services.AdsController;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.android.gms.common.api.CommonStatusCodes.SIGN_IN_REQUIRED;
import static com.google.android.gms.games.leaderboard.LeaderboardVariant.COLLECTION_PUBLIC;
import static com.google.android.gms.games.leaderboard.LeaderboardVariant.TIME_SPAN_ALL_TIME;

public class AndroidLauncher extends AndroidApplication implements AdsController, ServicesController {

    private String bannerAdUnitId = "";
    private String interstitialAdUnitId = "";
    private String interstitialVideoId = "";

    private int RC_SIGN_IN = 1;
    // -- Leaderboard variables
    private static final int RC_LEADERBOARD_UI = 9004;
    private static final String LOG_TAG = "ANDROID_APP";

    private AdView bannerAd;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount signedInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdsIdentifier();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        View gameView = initializeForView(new GdxGame(this), config);
        setupAds();
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layout.addView(bannerAd, params);

        setContentView(layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            hideVirtualButtons();
        }

        // Create the client used to sign in to Google services.
        mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());
    }

    private void initAdsIdentifier() {
        boolean isDebuggable = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            List<String> testDeviceIds = Collections.singletonList(getString(R.string.test_device_id));
            RequestConfiguration configuration =
                    new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
            MobileAds.setRequestConfiguration(configuration);
            bannerAdUnitId = getString(R.string.test_ad_banner_id);
            interstitialAdUnitId = getString(R.string.test_ad_interstitial_id);
            interstitialVideoId = getString(R.string.test_ad_interstitial_video_id);
        } else {
            bannerAdUnitId = getString(R.string.ad_banner_id);
            interstitialAdUnitId = getString(R.string.ad_interstitial_id);
            interstitialVideoId = getString(R.string.ad_interstitial_video_id);
        }
    }

    public void setupAds() {
        bannerAd = new AdView(this);
        bannerAd.setVisibility(View.INVISIBLE);
        bannerAd.setBackgroundColor(0xff000000); // black
        bannerAd.setAdUnitId(bannerAdUnitId);
        bannerAd.setAdSize(AdSize.BANNER);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(interstitialAdUnitId);
        interstitialAd.loadAd(new AdRequest.Builder().build());

        //AdMob Rewarded Video
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        setRewardVideoListener();
        rewardedVideoAd.loadAd(interstitialVideoId, new AdRequest.Builder().build());
    }

    @Override
    public boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null) return false;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }

    @Override
    public boolean isInterstitialLoaded() {
        return interstitialAd.isLoaded();
    }

    @Override
    public void showBannerAd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bannerAd.setVisibility(View.VISIBLE);
                AdRequest.Builder builder = new AdRequest.Builder();
                AdRequest ad = builder.build();
                bannerAd.loadAd(ad);
            }
        });
    }

    @Override
    public void hideBannerAd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bannerAd.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void showInterstitialAd(@Nullable final Runnable then) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (then != null) {
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            Gdx.app.postRunnable(then);
                            AdRequest.Builder builder = new AdRequest.Builder();
                            AdRequest ad = builder.build();
                            interstitialAd.loadAd(ad);
                        }
                    });
                }
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    AdRequest.Builder builder = new AdRequest.Builder();
                    AdRequest ad = builder.build();
                    interstitialAd.loadAd(ad);
                    if (then != null) Gdx.app.postRunnable(then);
                }

            }
        });
    }

    @TargetApi(19)
    private void hideVirtualButtons() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hideVirtualButtons();
            }
        }
    }

    @Override
    public void showVideoAd(@Nullable Runnable then) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rewardedVideoAd.isLoaded()) {
                    rewardedVideoAd.show();
                } else {
                    rewardedVideoAd.loadAd(interstitialVideoId, new AdRequest.Builder().build());
                }
            }
        });
    }

    private void setRewardVideoListener() {
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
    }

    @Override
    public void signIn() {
        signInSilently();
    }

    private void signInSilently() {
        Log.d(LOG_TAG, "signInSilently()");
        //GoogleSignInClient signInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d(LOG_TAG, "signInSilently(): success");
                            signedInAccount = task.getResult();
                            if (signedInAccount != null) {
                                GamesClient gamesClient = Games.getGamesClient(AndroidLauncher.this, signedInAccount);
                                gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                                gamesClient.setViewForPopups(((AndroidGraphics) AndroidLauncher.this.getGraphics()).getView());
                            }
                        } else {
                            Log.d(LOG_TAG, "signInSilently(): failure", task.getException());
                            // Player will need to sign-in explicitly using via UI if the silent sign-in fails
                            // with exception code of SIGN_IN_REQUIRED
                            ApiException signInFailException = (ApiException) task.getException();
                            if (signInFailException != null) {
                                int exceptionStatusCode = signInFailException.getStatusCode();
                                if (exceptionStatusCode == SIGN_IN_REQUIRED) {
                                    startSignInIntent();
                                }
                            }

                        }
                    }
                });
    }

    private void startSignInIntent() {
        //Manually sign in if silent sign-in fails
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Activity result for startSignInIntent method
        //If signed in successfully, get signed in account, otherwise log result fail code
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result == null) return;
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                // Set pop up notification for signed in account to display when user is signed in
                signedInAccount = result.getSignInAccount();
                if (signedInAccount != null) {
                    GamesClient gamesClient = Games.getGamesClient(AndroidLauncher.this, signedInAccount);
                    gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                    gamesClient.setViewForPopups(((AndroidGraphics) AndroidLauncher.this.getGraphics()).getView());
                }
            } else {
                int failCode = result.getStatus().getStatusCode();
                Log.d(LOG_TAG, "" + failCode);
                Log.d(LOG_TAG, "" + result.getStatus());
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.sign_in_exception_msg);
                }
                Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0,160);
                toast.show();
            }

        }
    }

    @Override
    public void signOut() {
        Log.d(LOG_TAG, "signOut()");

        if (!isSignedIn()) {
            Log.w(LOG_TAG, "signOut() called, but was not signed in!");
            return;
        }
        //Figure out when/if you need to allow the user to sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean successful = task.isSuccessful();
                        Log.d(LOG_TAG, "signOut(): " + (successful ? "success" : "failed"));
                    }
                });
    }

    @Override
    public void submitScore(long highScore) {
        //Ensure user is signed in so game doesn't crash
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account == null) return;
            String id = getString(R.string.leaderboard);
            Games.getLeaderboardsClient(this, account).submitScore(id, highScore);
        }
    }

    @Override
    public void showLeaderboard() {
        //Ensure user is signed in so game doesn't crash
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account == null) return;
            String id = getString(R.string.leaderboard);
            Games.getLeaderboardsClient(this, account)
                    .getLeaderboardIntent(id)
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_LEADERBOARD_UI);
                        }
                    });
        }
    }

    @Override
    public void getPlayerCenteredScores(@NotNull final CallBack callBack) {

        //Get a list of player centered high scores and return it in ArrayList<String> format

        //Ensure user is signed in so game doesn't crash
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String id = getString(R.string.leaderboard);
            Task<AnnotatedData<LeaderboardsClient.LeaderboardScores>> playerCenteredScoresTask =
                    Games.getLeaderboardsClient(this, account)
                            .loadPlayerCenteredScores(id, TIME_SPAN_ALL_TIME, COLLECTION_PUBLIC, 10, false);

            playerCenteredScoresTask.addOnSuccessListener(new OnSuccessListener<AnnotatedData<LeaderboardsClient.LeaderboardScores>>() {
                @Override
                public void onSuccess(AnnotatedData<LeaderboardsClient.LeaderboardScores> leaderboardScoresAnnotatedData) {

                    LeaderboardsClient.LeaderboardScores leaderboardScores = leaderboardScoresAnnotatedData.get();
                    if (leaderboardScores == null) return;
                    LeaderboardScoreBuffer leaderboardScoreBuffer = leaderboardScores.getScores();
                    int count = leaderboardScoreBuffer.getCount();
                    ArrayList<String> playerCenteredHighScores = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        LeaderboardScore score = leaderboardScoreBuffer.get(i);
                        String scoreString = "";
                        scoreString += "Name: " + score.getScoreHolderDisplayName() +
                                " Rank: " + score.getDisplayRank() + " Score: " + score.getDisplayScore();
                        playerCenteredHighScores.add(scoreString);
                    }
                    leaderboardScoreBuffer.release();
                    callBack.success(playerCenteredHighScores);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callBack.fail(e, getString(R.string.leaderboard_exception));
                }
            });

        }
    }

    @Override
    public void getTopScores(int scoreType, @NotNull final CallBack callBack) {
        //Get a list of top high scores and return it in ArrayList<String> format

        //Ensure user is signed in so game doesn't crash
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String id = getString(R.string.leaderboard);
            Task<AnnotatedData<LeaderboardsClient.LeaderboardScores>> topScoresTask =
                    Games.getLeaderboardsClient(this, account)
                            .loadTopScores(id, TIME_SPAN_ALL_TIME, COLLECTION_PUBLIC, 20, false);

            topScoresTask.addOnSuccessListener(new OnSuccessListener<AnnotatedData<LeaderboardsClient.LeaderboardScores>>() {
                @Override
                public void onSuccess(AnnotatedData<LeaderboardsClient.LeaderboardScores> leaderboardScoresAnnotatedData) {

                    LeaderboardsClient.LeaderboardScores leaderboardScores = leaderboardScoresAnnotatedData.get();
                    if (leaderboardScores == null) return;
                    LeaderboardScoreBuffer leaderboardScoreBuffer = leaderboardScores.getScores();
                    int count = leaderboardScoreBuffer.getCount();
                    ArrayList<String> topHighScores = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        LeaderboardScore score = leaderboardScoreBuffer.get(i);
                        String scoreString = "";
                        scoreString += "Name: " + score.getScoreHolderDisplayName() +
                                " Rank: " + score.getDisplayRank() + " Score: " + score.getDisplayScore();
                        topHighScores.add(scoreString);
                    }
                    leaderboardScoreBuffer.release();
                    //Send callback to mobile device with scores requested
                    callBack.success(topHighScores);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callBack.fail(e, getString(R.string.leaderboard_exception));
                }
            });

        }
    }

    @Override
    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    @Override
    public void unlockAchievement(@NotNull Config.Achievement achievement) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        String id = getId(achievement);
        if (id != null && account != null) {
            Games.getAchievementsClient(this, account).unlock(id);
        }
    }

    private String getId(@NotNull Config.Achievement achievement) {
        Context context = getApplicationContext();
        int identifier = context.getResources().getIdentifier(achievement.name(),
                "string", context.getPackageName());
        if (identifier == 0) {
            Log.w(LOG_TAG, "Achievement id not found in string resources");
            return null;
        }
        return context.getResources().getString(identifier);
    }

    @Override
    public void incrementAchievement(@NotNull Config.Achievement achievement, int value) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        String id = getId(achievement);
        if (id != null && account != null) {
            Games.getAchievementsClient(this, account).increment(id, value);
        }
    }

    @Override
    public void showAllAchievements() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account == null) return;
        final int RC_ACHIEVEMENT_UI = 9003;
        Games.getAchievementsClient(this, account)
                .getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                    }
                });
    }

    @Override
    public void share(int score) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String msg = getString(R.string.share_msg, score) + getString(R.string.app_link); //TODO replace link after publishing
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,getString(R.string.share_window_header)));
    }
}
