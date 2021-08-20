package io.kokoichi.sample.rhythmgame;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class MyMediaPlayer implements MediaPlayer.OnCompletionListener {

    MediaPlayer player = null;

    public MyMediaPlayer(Context applicationContext, int musicResId) {

        player = MediaPlayer.create(applicationContext, R.raw.test_sound);
        Log.d("hoge", "ko");

        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("hoge", "IOException error");
        } catch (Exception e) {
            Log.d("hoge", String.valueOf(e));
        }
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        // TODO:
        // Tell view Activity to return to home Activity
        Log.d("hoge", "Complete the media");
    }
}
