package io.kokoichi.sample.rhythmgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static io.kokoichi.sample.rhythmgame.GameView.screenRatioX;

public class Circle {

    Bitmap circle;
    int x, y, length;

    Circle(Resources res) {

        circle = BitmapFactory.decodeResource(res, R.drawable.circle);

        length = circle.getWidth();

        length /= 2;

        length = (int) (length * screenRatioX);

        circle = Bitmap.createScaledBitmap(circle, length, length, false);

    }
}
