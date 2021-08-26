package io.kokoichi.sample.rhythmgame;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameViewTest {

    @Rule
    public ActivityTestRule<GameActivity> mActivityGameTestRule = new ActivityTestRule<GameActivity>(GameActivity.class);

    private GameActivity mActivityGame = null;

    private GameView gameView;

    @Before
    public void setUp() throws Exception {

        mActivityGame = mActivityGameTestRule.getActivity();
        gameView = mActivityGame.gameView;

    }

    @Test
    public void testInstanceNotNull() {

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

        for (Object object: objectsInGameView) {
            assertNotNull(object);
        }
    }

    @Test
    public void newNotes() {

        int num_before = gameView.notesList.size();
        gameView.newNotes(2);
        int num_after = gameView.notesList.size();
        assertEquals(1, num_after - num_before);

    }

    @After
    public void tearDown() throws Exception {

        mActivityGame = null;
        gameView = null;
    }
}