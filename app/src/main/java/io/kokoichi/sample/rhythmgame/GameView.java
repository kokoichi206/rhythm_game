package io.kokoichi.sample.rhythmgame;

import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    public GameView(GameActivity activity, int screenX, int screenY){
        super(activity);
    }

    @Override
    public void run() {
        update();
        draw();
        sleep();
    }

    private void sleep() {
    }

    private void update() {
    }

    private void draw() {
    }
}
