package io.kokoichi.sample.rhythmgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_1 = 1;
    private DatabaseHelper dbHelper = new DatabaseHelper(this);

    public static final String INTENT_KEY_MAX_COMBO = "maxCombo";

    private int max_combo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.play).setOnClickListener((view) -> {
            startActivityForResult(new Intent(MainActivity.this, GameActivity.class), REQUEST_CODE_1);
        });

        deleteRecord(getString(R.string.music_1));
        insertCombo(getString(R.string.music_1), 2);
        // Get the max combo
        max_combo = getHighCombo();

        // Display the max combo
        String message = String.format(getString(R.string.display_high_score), max_combo);
        TextView combo_text = findViewById(R.id.display_high_score);
        combo_text.setText(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Return from gameActivity
            case (REQUEST_CODE_1):
                if (resultCode == RESULT_OK) {
                    int max_combo_last_game = data.getIntExtra(INTENT_KEY_MAX_COMBO, 0);
                    if (max_combo_last_game > max_combo) {
                        Log.d("hoge", "The max_combo is updated");
                        max_combo = max_combo_last_game;
                        updateRecord(getString(R.string.music_1), max_combo);
                        displayMaxCombo();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                } else {
                }
                break;
            default:
                break;
        }
    }

    private void displayMaxCombo() {
        // Display the max combo
        String message = String.format(getString(R.string.display_high_score), max_combo);
        TextView combo_text = findViewById(R.id.display_high_score);
        combo_text.setText(message);
    }

    @Override
    protected void onDestroy () {
        dbHelper.close();
        super.onDestroy();
    }

    /**
     * Get music high score (combo) from local db
     *
     * @return
     */
    private int getHighCombo () {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor c = db.rawQuery("select * from table where column = ?",new String[]{"data"});
        int combo = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from PlayRecords where name = ?", new String[]{getString(R.string.music_1)});
            int id[] = new int[cursor.getCount()];
            int i = 0;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int idx = cursor.getColumnIndex("combo");
                combo = Integer.parseInt(cursor.getString(idx));
            } else {
                combo = -1;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return combo;
    }

    /**
     *
     * @param music
     * @param combo
     * @return the number of lines (if not found, return -1)
     */
    private long insertCombo (String music,int combo){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", music);
        values.put("combo", combo);

        return db.insert(dbHelper.TABLE_NAME, null, values);
    }

    private boolean deleteRecord (String music){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.delete(dbHelper.TABLE_NAME, "name = '" + music + "'", null) == 1;
    }

    private long updateRecord (String music,int combo){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", music);
        values.put("combo", combo);

        String whereClause = "name = '" + music + "'";

        return db.update(dbHelper.TABLE_NAME, values, whereClause, null);
    }
}