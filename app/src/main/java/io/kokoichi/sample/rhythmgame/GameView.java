package io.kokoichi.sample.rhythmgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceView;

import java.util.ArrayList;

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
    private ArrayList<Notes> notesList;
    private Position[] positions;

    private class Position {
        int x,y;
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
        int NOTES_NUM = 5;
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
        newNotes();

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

    public void newNotes() {

        Notes notes = new Notes(getResources());
        notes.x = 200;
        notes.y = 0;
        notesList.add(notes);

    }
}
