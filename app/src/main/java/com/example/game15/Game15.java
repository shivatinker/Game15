package com.example.game15;

import android.content.AbstractThreadedSyncAdapter;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game15 {
    public static final int FREE_BLOCK = 0;
    private final int size;
    private final int total;
    private int[][] field;
    private int moves = 0;

    public Game15(int size) {
        this.size = size;
        this.field = new int[size][size];
        total = size * size;
        Random r = new Random();
        ArrayList<Integer> values = new ArrayList<>();
        values.add(FREE_BLOCK);
        for (int i = 1; i < total; i++)
            values.add(i);
        Collections.shuffle(values, r);
        for (int i = 0; i < total; i++)
            field[i / 4][i % 4] = values.get(i);
        if (!canSolve()) {
            System.out.println("Flipping..");
            for (int i = 0; i < total - 1; i++)
                if (field[i / 4][i % 4] != FREE_BLOCK && field[(i + 1) / 4][(i + 1) % 4] != FREE_BLOCK) {
                    int tmp = field[i / 4][i % 4];
                    field[i / 4][i % 4] = field[(i + 1) / 4][(i + 1) % 4];
                    field[(i + 1) / 4][(i + 1) % 4] = tmp;
                    break;
                }
        }
        if (!canSolve())
            System.err.println("Oops.");
    }

    private boolean canSolve() {
        int inv = 0;
        for (int i = 0; i < total; i++)
            if (field[i / 4][i % 4] != FREE_BLOCK)
                for (int j = 0; j < i; j++)
                    if (field[j / 4][j % 4] > field[i / 4][i % 4])
                        inv++;
        for (int i = 0; i < total; i++)
            if (field[i / 4][i % 4] == FREE_BLOCK)
                inv += 1 + i / 4;
        return inv % 2 == 0;
    }

    public int[][] getField() {
        return field;
    }

    private boolean checkIndex(int x, int y) {
        return !(x < 0 || y < 0 || x >= size || y >= size);
    }

    public boolean isWon() {
        for (int i = 0; i < total - 1; i++)
            if (field[i / 4][i % 4] != i + 1)
                return false;
        return true;
    }

    /**
     * @param x clicked block x
     * @param y clicked block y
     * @return <code>true</code>, if board has changed, <code>false</code> instead
     */
    public boolean doMove(int x, int y) {
        if (!checkIndex(x, y))
            throw new ArrayIndexOutOfBoundsException();
        boolean ok = false;
        ArrayList<Pair<Integer, Integer>> coords = new ArrayList<>();
        coords.add(new Pair<>(x, y + 1));
        coords.add(new Pair<>(x, y - 1));
        coords.add(new Pair<>(x + 1, y));
        coords.add(new Pair<>(x - 1, y));
        for (Pair<Integer, Integer> p : coords)
            if (checkIndex(p.first, p.second) && field[p.first][p.second] == FREE_BLOCK) {
                field[p.first][p.second] = field[x][y];
                field[x][y] = FREE_BLOCK;
                ok = true;
            }
        if (ok)
            moves++;
        return ok;
    }

    public int getMoves() {
        return moves;
    }
}
