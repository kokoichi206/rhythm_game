package io.kokoichi.sample.rhythmgame;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GameViewTest {

    @Rule
    public ActivityTestRule<GameActivity> mActivityGameTestRule = new ActivityTestRule<GameActivity>(GameActivity.class);

    private GameActivity mActivityGame = null;

    private GameView gameView;

    // There Objects have to be initialized in onCreate
    Object[] objectsInGameView = {
            gameView.activity,
            gameView.positions,
            gameView.notesList,
            gameView.button,
            gameView.hpBar,
            gameView.myPlayer,
            gameView.paint,
            gameView.sPaint,
            gameView.thread,
            gameView.background,
            gameView.circles,
            gameView.info,
    };

    @Before
    public void setUp() throws Exception {

        mActivityGame = mActivityGameTestRule.getActivity();
        gameView = new GameView(mActivityGame, mActivityGame.point.x, mActivityGame.point.y);

    }

    @Test
    public void testInstanceNotNull() {

        for (Object object: objectsInGameView) {
            assertNotNull(object);
        }

    }

    @After
    public void tearDown() throws Exception {

        mActivityGame = null;
        gameView = null;
    }
}