package com.example.game15;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static java.util.Locale.ENGLISH;

public class MainActivity extends Activity implements View.OnClickListener {

    private final int BLOCKS_SIDE_COUNT = 4;
    private Game15 game;

    private TableLayout fieldLayout;
    private RelativeLayout[][] fieldBlocks;
    private TextView movesText;

    private boolean freeze = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fieldLayout = findViewById(R.id.game_layout);
        movesText = findViewById(R.id.text_moves);
        fieldBlocks = new RelativeLayout[BLOCKS_SIDE_COUNT][BLOCKS_SIDE_COUNT];

        findViewById(R.id.btn_newgame).setOnClickListener((v) -> {
            game = new Game15(BLOCKS_SIDE_COUNT);
            update();
            freeze = false;
        });

        final int display_width = getResources().getDisplayMetrics().widthPixels;
        final int block_dim = display_width / BLOCKS_SIDE_COUNT;

        game = new Game15(BLOCKS_SIDE_COUNT);

        for (int i = 0; i < BLOCKS_SIDE_COUNT; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < BLOCKS_SIDE_COUNT; j++) {
                RelativeLayout block = new RelativeLayout(this);
                block.setGravity(Gravity.CENTER);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(block_dim, block_dim);
                layoutParams.column = j;
                block.setBackground(getDrawable(R.drawable.block_free));
                row.addView(block, layoutParams);
                fieldBlocks[i][j] = block;

                block.setOnClickListener(this);

                TextView label = new TextView(this);
                label.setText("");
                label.setTextSize(35);
                label.setTypeface(null, Typeface.BOLD);
                label.setTextColor(Color.BLACK);
                block.addView(label);
            }
            TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            fieldLayout.addView(row, layoutParams);
        }

        update();
    }


    private void update() {
        int[][] field = game.getField();
        for (int i = 0; i < BLOCKS_SIDE_COUNT; i++)
            for (int j = 0; j < BLOCKS_SIDE_COUNT; j++) {
                RelativeLayout block = fieldBlocks[i][j];
                block.setBackground(getDrawable(field[i][j] == Game15.FREE_BLOCK ? R.drawable.block_free : R.drawable.block));
                TextView label = (TextView) block.getChildAt(0);
                label.setText(field[i][j] == Game15.FREE_BLOCK ? "" : String.format(ENGLISH, "%d", field[i][j]));
            }
        movesText.setText(String.format(ENGLISH, "Moves: %d", game.getMoves()));

        if (game.isWon()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(String.format(ENGLISH, "You won in %d moves!", game.getMoves()))
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                    });
            AlertDialog alert = builder.create();
            alert.show();
            freeze = true;
        }
    }

    @Override
    public void onClick(View view) {
        if (freeze)
            return;
        for (int i = 0; i < BLOCKS_SIDE_COUNT; i++)
            for (int j = 0; j < BLOCKS_SIDE_COUNT; j++) {
                if (fieldBlocks[i][j].equals(view)) {
                    game.doMove(i, j);
                    update();
                    return;
                }
            }
    }
}
