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

    @Test
    public void getCircleIndex() {

        class RelativePosition {
            int rel_x, rel_y;
            RelativePosition(int x, int y) {
                rel_x = x;
                rel_y = y;
            }
        }

        for (int i = 0; i < gameView.circles.length; i++) {

            Circle circle = gameView.circles[i];
            assertNotNull(circle);
            int startX = circle.x;
            int startY = circle.y;
            int width = circle.length;
            int height = circle.length;

            int edge = 1;

            RelativePosition[] verifyRelativePositions = {
                    new RelativePosition(edge, edge),
                    new RelativePosition(width / 2, height / 2),
                    new RelativePosition(width - edge, height - edge),
            };

            for (RelativePosition relativePosition: verifyRelativePositions) {

                float x = startX + relativePosition.rel_x;
                float y = startY + relativePosition.rel_y;
                int circleIndex = gameView.getCircleIndex(x, y);
                assertEquals("x position: " + relativePosition.rel_x, i, circleIndex);
            }
        }

        // Out of range test: expected -1 as a return value
        int startX = 0;
        int startY = 0;
        RelativePosition[] verifyRelativePositions = {
                new RelativePosition(20, 20),
                new RelativePosition(400, 20),
                new RelativePosition(-4, 5),
                new RelativePosition(4, -50),
        };
        for (RelativePosition relativePosition: verifyRelativePositions) {

            float x = startX + relativePosition.rel_x;
            float y = startY + relativePosition.rel_y;
            int circleIndex = gameView.getCircleIndex(x, y);
            assertEquals("x position: " + relativePosition.rel_x, -1, circleIndex);
        }
    }

    @After
    public void tearDown() throws Exception {

        mActivityGame = null;
        gameView = null;
    }
}