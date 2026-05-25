package com.example.game2048.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Core 2048 game board logic.
 */
public class GameBoard {

    private static final int SIZE = 4;
    private final int[][] grid = new int[SIZE][SIZE];
    private int score;
    private boolean gameOver;
    private boolean won;
    private final Random random = new Random();

    public GameBoard() {
        reset();
    }

    /** Reset board, place two initial tiles. */
    public void reset() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = 0;
            }
        }
        score = 0;
        gameOver = false;
        won = false;
        spawnTile();
        spawnTile();
    }

    /* ---------- public moves ---------- */

    public boolean moveUp()    { return slide(true, false); }
    public boolean moveDown()  { return slide(true, true); }
    public boolean moveLeft()  { return slide(false, false); }
    public boolean moveRight() { return slide(false, true); }

    /* ---------- internal ---------- */

    /**
     * @param vertical true = up/down, false = left/right
     * @param reverse  true = down/right, false = up/left
     * @return true if the board changed
     */
    private boolean slide(boolean vertical, boolean reverse) {
        boolean changed = false;

        for (int i = 0; i < SIZE; i++) {
            // extract line
            int[] line = new int[SIZE];
            for (int j = 0; j < SIZE; j++) {
                line[j] = vertical ? grid[j][i] : grid[i][j];
            }

            int[] merged = merge(line, reverse);

            // write back
            for (int j = 0; j < SIZE; j++) {
                int val = merged[j];
                if (vertical) {
                    if (grid[j][i] != val) changed = true;
                    grid[j][i] = val;
                } else {
                    if (grid[i][j] != val) changed = true;
                    grid[i][j] = val;
                }
            }
        }

        if (changed) {
            spawnTile();
            if (isFull()) {
                gameOver = !canMerge();
            }
        }
        return changed;
    }

    /** Merge a single row/column toward one end. */
    private int[] merge(int[] line, boolean reverse) {
        int[] work = new int[SIZE];
        // compact non-zero tiles
        int idx = 0;
        if (reverse) {
            for (int i = SIZE - 1; i >= 0; i--) {
                if (line[i] != 0) work[idx++] = line[i];
            }
        } else {
            for (int i = 0; i < SIZE; i++) {
                if (line[i] != 0) work[idx++] = line[i];
            }
        }

        // merge adjacent equal tiles
        int[] result = new int[SIZE];
        int ri = 0;
        if (reverse) {
            for (int i = 0; i < idx; i++) {
                if (i + 1 < idx && work[i] == work[i + 1]) {
                    int merged = work[i] * 2;
                    result[ri++] = merged;
                    score += merged;
                    if (merged == 2048) won = true;
                    i++; // skip next
                } else {
                    result[ri++] = work[i];
                }
            }
        } else {
            // forward direction — fill result from end so merged tiles stick to the target edge
            for (int i = idx - 1; i >= 0; i--) {
                if (i - 1 >= 0 && work[i] == work[i - 1]) {
                    int merged = work[i] * 2;
                    // write at position ri from the right
                    result[SIZE - 1 - ri] = merged;
                    ri++;
                    score += merged;
                    if (merged == 2048) won = true;
                    i--; // skip next
                } else {
                    result[SIZE - 1 - ri] = work[i];
                    ri++;
                }
            }
        }
        return result;
    }

    private void spawnTile() {
        List<int[]> empty = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] == 0) empty.add(new int[]{r, c});
            }
        }
        if (empty.isEmpty()) return;
        int[] cell = empty.get(random.nextInt(empty.size()));
        grid[cell[0]][cell[1]] = random.nextDouble() < 0.9 ? 2 : 4;
    }

    private boolean isFull() {
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                if (grid[r][c] == 0) return false;
        return true;
    }

    private boolean canMerge() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (c + 1 < SIZE && grid[r][c] == grid[r][c + 1]) return true;
                if (r + 1 < SIZE && grid[r][c] == grid[r + 1][c]) return true;
            }
        }
        return false;
    }

    /* ---------- getters ---------- */

    public int[][] getGrid() { return grid; }
    public int getScore() { return score; }
    public boolean isGameOver() { return gameOver; }
    public boolean isWon() { return won; }
}
