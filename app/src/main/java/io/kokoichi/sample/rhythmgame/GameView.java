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

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        this.screenX = screenX;
        this.screenY = screenY;

        // FIXME
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background = new Background(screenX, screenY, getResources());

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
