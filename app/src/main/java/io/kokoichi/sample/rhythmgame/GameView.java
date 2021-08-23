package io.kokoichi.sample.rhythmgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    String TAG = GameView.class.getSimpleName();

    private static final int SLEEP_TIME = Math.round(1000 / 60);
    private int NOTES_NUM = 5;  // type 1-NOTES_NUM

    public static float screenRatioX, screenRatioY;

    private Thread thread;
    private boolean isPlaying;
    private Paint paint, sPaint;
    private int screenX, screenY;
    private GameActivity activity;
    private Random random;

    private Background background;
    private Circle[] circles;
    private ArrayList<Notes> notesList;
    private Position[] positions;

    private MyMediaPlayer myPlayer;
    private double[] dropTiming;
    private double musicStartingTime, musicEndingTime;
    private int num_bar;

    private int combo = 0;
    private static int maxCombo = 0;
    private Info info;      // information like "GOOD","PERFECT"
    private static final int JUDGE_INFO_AGE = 15;

    private class Info {
        int age;            // the unit is loop count
        String message;        // message like "GOOD","PERFECT"
    }

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
        paint.setTextSize(128);
        paint.setColor(Color.BLACK);

        // FIXME: There should be better ways
        sPaint = new Paint();       // for SMALL text
        sPaint.setTextSize(84);
        sPaint.setColor(Color.GRAY);

        info = new Info();
        info.age = 0;
        info.message = "";

        random = new Random();

        // Sound init
        // CHOOSE ONE MUSIC
        // All time related variables are milliseconds
        myPlayer = new MyMediaPlayer(activity, R.raw.kimigayo, this);
        musicStartingTime = 5.108866213151927 * 1000;
        musicEndingTime = 45.24492063492064 * 1000;
        dropTiming = new double[]{0,
                1, 1, 1, 1, 1, 1, 2,
                1, 1, 1, 0.5, 0.5, 1, 1, 1, 1,
                1, 1, 2, 1, 1, 2,
                1, 1, 1, 1, 1.5, 0.5, 2,
                1, 1, 2, 1, 1, 1, 1,
                1, 0.5, 0.5, 2};
        num_bar = 11 * 4;

        // Time between appearance and the clickable zone
        int timeToCircle = Notes.lifeTimeMilliSec;
        dropTiming[0] += musicStartingTime - timeToCircle;

        // Convert one bar of music sheet to milliseconds
        double one_bar = (musicEndingTime - musicStartingTime) / num_bar;
        for (int i = 1; i < dropTiming.length; i++) {
            dropTiming[i] = dropTiming[i - 1] + dropTiming[i] * one_bar;
        }

    }

    @Override
    public void run() {

        myPlayer.player.start();
        myPlayer.player.setOnCompletionListener(myPlayer);

        // set the params for count the timing
        long startedAt = System.currentTimeMillis();
        Log.d(TAG, "loop started at " + startedAt);
        int notesIndex = 0;
        double nextNotesTiming = dropTiming[notesIndex];

        while (isPlaying) {

            update();
            draw();
//            sleep();

            // drop the notes when the time comes
            if (System.currentTimeMillis() - startedAt > nextNotesTiming) {

                // pass the type (NOT index)
                int notesType = random.nextInt(NOTES_NUM) + 1;
                if (notesType >= 1 && notesType <= NOTES_NUM) {
                    newNotes(notesType);
                } else {
                    newNotes(1);
                }

                notesIndex += 1;
                nextNotesTiming = dropTiming[notesIndex];
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

            if (notes.age > (notes.lifeTimeMilliSec + notes.OFFSET)) {
                trashNotes.add(notes);
            }
        }
        if (trashNotes.size() > 0) {
            for (Notes notes : trashNotes) {
                notesList.remove(notes);
            }
            maxCombo = (combo > maxCombo) ? combo : maxCombo;
            combo = 0;
        }

        // info: decrease age
        if (info.age > 0) {
            info.age -= 1;
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

        // draw combo if combo > 0
        if (combo > 0) {
            drawTextCenter(canvas, combo + "", screenX / 2f, 164, paint);
        }

        // draw info if message exists
        if (info.age > 0) {
            drawTextCenter(canvas, info.message + "", screenX / 2f, 328, sPaint);
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

        // return if touched point is out of circle area
        int index = getCircleIndex(touchedX, touchedY);
        if (index == -1) {
            Log.d(TAG, "touched point is OUT of the Circles");
            return true;
        }
        // adjust touched point to the center of the circle
        touchedX = circles[index].x + circles[index].length / 2;
        touchedY = circles[index].y + circles[index].length / 2;

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                Notes touchedNotes = null;

                for (Notes notes : notesList) {

                    double dist = Math.pow(notes.x + notes.length / 2 - touchedX, 2)
                            + Math.pow(notes.y + notes.length / 2 - touchedY, 2);
                    if (dist < 1000) {
                        Log.d("hoge", "PERFECT");
                        touchedNotes = notes;
                        updateInfo("PERFECT", JUDGE_INFO_AGE);
                    } else if (dist < 1500) {
                        Log.d("hoge", "GOOD");
                        touchedNotes = notes;
                        updateInfo("GOOD", JUDGE_INFO_AGE);
                    } else if (dist < 3000) {
                        Log.d("hoge", "OK");
                        touchedNotes = notes;
                        updateInfo("OK", JUDGE_INFO_AGE);
                    }
                }

                if (touchedNotes != null) {
                    notesList.remove(touchedNotes);
                    combo += 1;
                    Log.d("hoge", "combo is " + combo);
                }

                break;
        }

        return true;
    }

    private void drawTextCenter(Canvas canvas, String text, float x, float y, Paint paint) {
        float width = paint.measureText(text);
        float startX = x - width / 2;
        float startY = y - (paint.getFontMetrics().descent + paint.getFontMetrics().ascent) / 2;
        canvas.drawText(text, startX, startY, paint);
    }

    private void updateInfo(String msg, int age) {
        info.age = age;
        info.message = msg;
    }

    private int getCircleIndex(float touchedX, float touchedY) {

        int index = -1;
        for (int i = 0; i < NOTES_NUM; i++) {
            if (((touchedX > circles[i].x) && (touchedX < circles[i].x + circles[i].length)) &&
                    ((touchedY > circles[i].y) && (touchedY < circles[i].y + circles[i].length))) {
                index = i;
            }
        }

        return index;
    }

    public int getMaxCombo() {
        return maxCombo;
    }
}
