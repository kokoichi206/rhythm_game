package io.kokoichi.sample.rhythmgame;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

    // To preserve current data
    int current_max_combo;

    @Before
    public void setUp() throws Exception {

        mActivity = mActivityTestRule.getActivity();

        current_max_combo = mActivity.getHighCombo();
    }

    @Test
    public void isMeStateValid() {

        assertTrue(mActivity.me.rank >= 1);
        assertTrue(mActivity.me.exp >= 0);

    }

    @Test
    public void testLaunch() {

        for (int id: ids) {
            View view = mActivity.findViewById(id);
            assertNotNull(view);
        }
    }
    

    @Test
    public void getHighCombo() {

        int testCombo = mActivity.getHighCombo();
        assertTrue(testCombo >= 0);
    }

    @Test
    public void updateRecord() {

        int[] combos = {1, 11, 121, 12321, 11111111};

        for (int combo: combos) {
            mActivity.updateRecord(mActivity.getString(R.string.music_1), combo);
            assertTrue(combo == mActivity.getHighCombo());
        }
    }

    @After
    public void tearDown() throws Exception {

        mActivity.updateRecord(mActivity.getString(R.string.music_1), current_max_combo);

        mActivity = null;

    }
}