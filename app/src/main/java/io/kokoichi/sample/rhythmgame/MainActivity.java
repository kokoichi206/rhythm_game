package io.kokoichi.sample.rhythmgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.play).setOnClickListener((view) -> {
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        });

        deleteRecord("kimigayo");
        insertCombo("kimigayo", 33);
        // Get and Display the max combo
        int max_combo = getHighCombo();
        String message = String.format(getString(R.string.display_high_score), max_combo);
        TextView combo_text = findViewById(R.id.display_high_score);
        combo_text.setText(message);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    /**
     * Get music high score (combo) from local db
     *
     * @return
     */
    private int getHighCombo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor c = db.rawQuery("select * from table where column = ?",new String[]{"data"});
        int combo = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from PlayRecords where name = ?",new String[]{"kimigayo"});
            int id[] = new int[cursor.getCount()];
            int i = 0;
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();

                int idx = cursor.getColumnIndex("combo");
                combo = Integer.parseInt(cursor.getString(idx));
            } else {
                combo = -1;
            }
        }
        finally {
            if(cursor != null) {
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
    private long insertCombo(String music, int combo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", music);
        values.put("combo", combo);

        return db.insert(dbHelper.TABLE_NAME, null, values);
    }

    private boolean deleteRecord(String music) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.delete(dbHelper.TABLE_NAME, "name = '" + music + "'", null) == 1;
    }

    private long updateRecord(String music, int combo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", music);
        values.put("combo", combo);

        String whereClause = "name = '" + music + "'";

        return db.update(dbHelper.TABLE_NAME, values, whereClause,null);
    }
}