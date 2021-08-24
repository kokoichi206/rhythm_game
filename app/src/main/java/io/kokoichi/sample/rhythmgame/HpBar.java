package io.kokoichi.sample.rhythmgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static io.kokoichi.sample.rhythmgame.GameView.screenRatioX;
import static io.kokoichi.sample.rhythmgame.GameView.screenRatioY;

public class HpBar {

    int x = 100;
    int y = 80;
    int width = 400;
    int height = 80;
    Bitmap hp_bar, max_hp_bar;
    int max_hp;

    HpBar (Resources res) {

        hp_bar = BitmapFactory.decodeResource(res, R.drawable.hp_bar);
        max_hp_bar = BitmapFactory.decodeResource(res, R.drawable.max_hp_bar);

        x = (int) (x * screenRatioX);
        y = (int) (y * screenRatioY);

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        hp_bar = Bitmap.createScaledBitmap(hp_bar, width, height, false);
        max_hp_bar = Bitmap.createScaledBitmap(max_hp_bar, width, height, false);
    }
}
