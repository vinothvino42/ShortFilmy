package com.example.vinothvino.shortfilmy;

/**
 * Created by vinothvino on 13/01/16.
 */

import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;

//Youtubeview
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;

//youtubethumbnailview
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;

public class YouTubeView extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{//,YouTubeThumbnailView.OnInitializedListener {

    public static final int RECOVER_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer youTubePlayer;
  //  private YouTubeThumbnailView youTubeThumbnailView;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_view);


        //Youtubeview
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.API_KEY, this);

    }


    //Youtubeplayerview onInitializationsuccess
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {

        //Creating bundle and store video id
        Bundle videoIdFromMainActiviy = getIntent().getExtras();
        String videoID = videoIdFromMainActiviy.getString("key");
       // String lyk = videoIdFromMainActiviy.getString("lykval");

        youTubePlayer = player;
        youTubePlayer.setFullscreen(true);

        player.setPlaybackEventListener(myPlaybackEvent);
        player.setPlayerStateChangeListener(myPlayerState);

        if (!wasRestored) {
            player.cueVideo(videoID);
        }
    }

    private PlaybackEventListener myPlaybackEvent = new PlaybackEventListener() {
        @Override
        public void onPlaying() {

           // Toast.makeText(getApplicationContext(), "Playing...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {

          //  Toast.makeText(getApplicationContext(), "Paused...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStopped() {

           // Toast.makeText(getApplicationContext(), "Stopped...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    private PlayerStateChangeListener myPlayerState = new PlayerStateChangeListener() {
        @Override
        public void onLoading() {

          //  Toast.makeText(getApplicationContext(), "Please be patient... ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

            Toast.makeText(getApplicationContext(), "Please be patient", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVideoStarted() {

          //  Toast.makeText(getApplicationContext(), "Video started", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVideoEnded() {

            Toast.makeText(getApplicationContext(), "See some other videos", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    //Youtubeplayer onInitializationFailure
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {

        if (errorReason.isUserRecoverableError()) {

            errorReason.getErrorDialog(this, RECOVER_REQUEST).show();

        } else {

            String error = String.format(getString(R.string.error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();

        }
    }

    //Checking requestcode if match,it restarts the initialization
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {

        if (requestcode == RECOVER_REQUEST) {

            getYoutubePlayerProvider().initialize(Config.API_KEY, this);
        }

    }

    protected Provider getYoutubePlayerProvider() {

        return youTubeView;

    }

}

