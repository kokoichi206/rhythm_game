package io.kokoichi.sample.rhythmgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import static androidx.core.content.ContextCompat.startActivity;

public class MyMediaPlayer implements MediaPlayer.OnCompletionListener {
    String TAG = MyMediaPlayer.class.getSimpleName();

    MediaPlayer player = null;
    Activity activity;

    public MyMediaPlayer(Activity activity, int musicResId) {

        player = MediaPlayer.create(activity.getApplicationContext(), R.raw.test_sound);

        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException error");
        } catch (Exception e) {
            Log.d(TAG, String.valueOf(e));
        }
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        // TODO:
        // Tell view Activity to return to home Activity
        Log.d(TAG, "Completion of the media");

        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }
}
