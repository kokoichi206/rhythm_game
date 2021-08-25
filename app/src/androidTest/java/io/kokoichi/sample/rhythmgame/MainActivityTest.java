package io.kokoichi.sample.rhythmgame;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MainActivityTest {

    // To test the activity

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity = null;

    private int[] ids = {
            R.id.background_home,
            R.id.play,
            R.id.display_high_score,
            R.id.max_hp_bar,
            R.id.hp_bar,
            R.id.exp_txt,
            R.id.display_rank_num,
            R.id.display_rank_txt,
    };

    @Before
    public void setUp() throws Exception {

        mActivity = mActivityTestRule.getActivity();

    }

    @Test
    public void testLaunch() {

        for (int id: ids) {
            View view = mActivity.findViewById(id);
            assertNotNull(view);
        }
    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;
    }
}