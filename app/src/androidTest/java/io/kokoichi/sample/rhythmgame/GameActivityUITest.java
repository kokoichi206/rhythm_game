package io.kokoichi.sample.rhythmgame;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class GameActivityUITest {

    @Rule
//    public ActivityScenario<GameActivity> mActivityGameTestRule = new ActivityScenario.launch(GameActivity.class);
    public ActivityTestRule<GameActivity> mActivityGameTestRule = new ActivityTestRule<>(GameActivity.class);

    private GameActivity mActivityGame = null;

    @Before
    public void setUp() {

        Intents.init();
        mActivityGame = mActivityGameTestRule.getActivity();

    }


    @Test
    public void testDeadDialog() {

        // Reduce hp
        mActivityGame.gameView.hpBar.current_hp = 1;

        // the dead dialog is displayed
        onView(withText(R.string.dead_dialog_message))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        // Click the ok button in
        onView(withText(R.string.dead_dialog_ok))
                .perform(click());

        // Check if the GameActivity finishes
        assertTrue(mActivityGameTestRule.getActivity().isFinishing());

        // The music is stopped
        assertFalse(mActivityGame.gameView.myPlayer.player.isPlaying());
    }

    @After
    public void tearDown() throws Exception {

        mActivityGame = null;
        Intents.release();

    }
    
}
