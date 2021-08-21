package io.kokoichi.sample.rhythmgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static io.kokoichi.sample.rhythmgame.GameView.screenRatioX;

public class Notes {

    Bitmap notes;
    int x, y, length, yLimit;
    public static int lifeTimeMilliSec = 1000;
    int age = 0;
    int OFFSET = 180;

    Notes(Resources res) {

        notes = BitmapFactory.decodeResource(res, R.drawable.notes);

        length = notes.getWidth();

        length /= 2;

        length = (int) (length * screenRatioX);

        notes = Bitmap.createScaledBitmap(notes, length, length, false);

        yLimit -= length;
    }

}
