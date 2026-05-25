package com.example.game2048;

import com.example.game2048.model.GameBoard;
import com.example.game2048.model.GameState;
import com.example.game2048.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Game2048ApplicationTests {

    @Autowired
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService.reset();
    }

    @Test
    void contextLoads() {
        assertNotNull(gameService);
    }

    @Test
    void initialStateHasTwoTiles() {
        GameState state = gameService.getState();
        int count = countNonZero(state.getGrid());
        assertEquals(2, count);
    }

    @Test
    void boardChangesOnValidMove() {
        GameState before = gameService.getState();
        int beforeCount = countNonZero(before.getGrid());

        GameState after = gameService.move("down");
        int afterCount = countNonZero(after.getGrid());

        // a move either merges or adds a tile, so count should be >= beforeCount
        assertTrue(afterCount >= beforeCount, "Board should have same or more tiles after a valid move");
    }

    @Test
    void invalidDirectionThrows() {
        assertThrows(IllegalArgumentException.class, () -> gameService.move("diagonal"));
    }

    @Test
    void resetClearsAndRestarts() {
        gameService.move("right");
        gameService.move("down");
        gameService.reset();

        GameState state = gameService.getState();
        assertEquals(0, state.getScore());
        assertFalse(state.isGameOver());
        assertFalse(state.isWon());
        assertEquals(2, countNonZero(state.getGrid()));
    }

    @Test
    void scoreNonNegative() {
        for (int i = 0; i < 20; i++) {
            gameService.move("up");
            gameService.move("right");
            gameService.move("down");
            gameService.move("left");
        }
        assertTrue(gameService.getState().getScore() >= 0);
    }

    private int countNonZero(int[][] grid) {
        int n = 0;
        for (int[] row : grid) {
            for (int v : row) {
                if (v != 0) n++;
            }
        }
        return n;
    }
}
