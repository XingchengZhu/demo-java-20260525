package com.example.game2048.model;

/**
 * Snapshot of game state returned to the frontend.
 */
public class GameState {
    private int[][] grid;
    private int score;
    private boolean gameOver;
    private boolean won;

    public GameState() {}

    public GameState(GameBoard board) {
        this.grid = board.getGrid();
        this.score = board.getScore();
        this.gameOver = board.isGameOver();
        this.won = board.isWon();
    }

    // getters / setters
    public int[][] getGrid() { return grid; }
    public void setGrid(int[][] grid) { this.grid = grid; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public boolean isWon() { return won; }
    public void setWon(boolean won) { this.won = won; }
}
