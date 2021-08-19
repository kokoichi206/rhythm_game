package io.kokoichi.sample.rhythmgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private static final int SLEEP_TIME = Math.round(1000 / 60);

    public static float screenRatioX, screenRatioY;

    private Thread thread;
    private boolean isPlaying;
    private Paint paint;
    private int screenX, screenY;
    private GameActivity activity;
    private Background background;
    private Circle[] circles;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        this.screenX = screenX;
        this.screenY = screenY;

        // FIXME
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        // Background init
        background = new Background(screenX, screenY, getResources());

        // Circle init
        circles = new Circle[5];

        for (int i = 0; i < 5; i++) {

            Circle circle = new Circle(getResources());
            // FIXME: remove magic number
            circle.x = screenX / 2 + (int) ((i - 2) * screenRatioX * 300);
            circle.y = screenY - (int) (screenRatioX * (300 + 12 * Math.pow(Math.abs(i - 2), 3)));
            circles[i] = circle;

        }

        paint = new Paint();
    }

    @Override
    public void run() {

        while(isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        return;
    }

    private void draw() {
        // draw background first
        Canvas canvas = getHolder().lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawBitmap(background.background, background.x, background.y, paint);

        // draw Birds
        for (Circle circle : circles) {
            canvas.drawBitmap(circle.circle, circle.x, circle.y, paint);
        }

        getHolder().unlockCanvasAndPost(canvas);
        return;
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
