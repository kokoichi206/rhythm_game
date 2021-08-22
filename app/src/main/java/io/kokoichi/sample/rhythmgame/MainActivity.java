package io.kokoichi.sample.rhythmgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
//        dbHelper.
        return 6;
    }
}