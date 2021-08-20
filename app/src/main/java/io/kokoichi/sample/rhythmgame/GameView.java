package io.kokoichi.sample.rhythmgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.min;

public class GameView extends SurfaceView implements Runnable {

    private static final int SLEEP_TIME = Math.round(1000 / 60);
    private int NOTES_NUM = 5;  // type 1-NOTES_NUM

    public static float screenRatioX, screenRatioY;

    private Thread thread;
    private boolean isPlaying;
    private Paint paint;
    private int screenX, screenY;
    private GameActivity activity;
    private Random random;

    private Background background;
    private Circle[] circles;
    private ArrayList<Notes> notesList;
    private Position[] positions;

    private class Position {
        int x, y;
    }

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        this.screenX = screenX;
        this.screenY = screenY;

        // FIXME
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        // Calculate notes position settings
        positions = new Position[NOTES_NUM];
        for (int i = 0; i < NOTES_NUM; i++) {

            Position position = new Position();
            // FIXME: remove magic number
            position.x = screenX / 2 + (int) ((i - 2) * screenRatioX * 300);
            position.y = screenY - (int) (screenRatioY * (300 + 12 * Math.pow(Math.abs(i - 2), 3)));
            positions[i] = position;

        }

        // Background init
        background = new Background(screenX, screenY, getResources());

        // Circle init
        circles = new Circle[NOTES_NUM];

        for (int i = 0; i < NOTES_NUM; i++) {

            Circle circle = new Circle(getResources());
            // FIXME: remove magic number
            float center = NOTES_NUM / 2;
            circle.x = positions[i].x;
            circle.y = positions[i].y;
            circles[i] = circle;

        }

        // Notes init
        notesList = new ArrayList<>();

        paint = new Paint();

        random = new Random();
    }

    @Override
    public void run() {

        while (isPlaying) {
            update();
            draw();
            sleep();
            // ランダムにノーツを落とす
            if (random.nextFloat() < 0.01) {
                newNotes(random.nextInt(NOTES_NUM) + 1);
            }
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
        // move notes
        ArrayList<Notes> trashNotes = new ArrayList<>();
        for (Notes notes : notesList) {
            notes.age += SLEEP_TIME;
            notes.y += notes.yLimit * SLEEP_TIME / notes.lifeTimeMilliSec;

            if (notes.age > notes.lifeTimeMilliSec) {
                trashNotes.add(notes);
            }
        }
        for (Notes notes : trashNotes) {
            notesList.remove(notes);
        }

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

        // draw Notes
        for (Notes notes : notesList) {
            canvas.drawBitmap(notes.notes, notes.x, notes.y, paint);
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

    /**
     * @param type position type (1-NOTES_NUM)
     */
    public void newNotes(int type) {

        int index = type - 1;

        Notes notes = new Notes(getResources());
        notes.x = positions[index].x;
        notes.y = 0;
        notes.yLimit = positions[index].y + notes.OFFSET;   // a little bit overshoot
        notesList.add(notes);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchedX = event.getX();
        float touchedY = event.getY();

        if (!isCircleTouched(touchedX, touchedY)) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                double mini = 9999999;
                for (Notes notes : notesList) {

                    double dist = Math.pow(notes.x + notes.length / 2 - touchedX, 2)
                            + Math.pow(notes.y + notes.length / 2 - touchedY, 2);
                    if (dist < 1000) {
                        Log.d("hoge", "PERFECT");
                    } else if (dist < 1500) {
                        Log.d("hoge", "GOOD");
                    } else if (dist < 2000) {
                        Log.d("hoge", "OK");
                    }
                    mini = min(mini, dist);
                }
                break;
        }

        return true;
    }

    private boolean isCircleTouched(float touchedX, float touchedY) {

        boolean isOnCircles = false;

        for (Circle circle : circles) {
            if (((touchedX > circle.x) && (touchedX < circle.x + circle.length)) &&
                    ((touchedY > circle.y) && (touchedY < circle.y + circle.length))) {
                isOnCircles = true;
            }
        }

        return isOnCircles;
    }
}
