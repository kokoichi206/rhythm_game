package io.kokoichi.sample.rhythmgame;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

public class GameActivityUITest {

    @Rule
    public ActivityTestRule<GameActivity> mActivityGameTestRule = new ActivityTestRule<GameActivity>(GameActivity.class);

    private GameActivity mActivityGame = null;

    @Before
    public void setUp() {
        mActivityGame = mActivityGameTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivityGame = null;
    }


}
