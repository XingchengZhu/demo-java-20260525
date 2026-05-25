package com.example.game2048.service;

import com.example.game2048.model.GameBoard;
import com.example.game2048.model.GameState;
import org.springframework.stereotype.Service;

/**
 * Service managing the singleton game session.
 */
@Service
public class GameService {

    private GameBoard board = new GameBoard();

    public GameState getState() {
        return new GameState(board);
    }

    public GameState reset() {
        board = new GameBoard();
        return new GameState(board);
    }

    public GameState move(String direction) {
        boolean changed;
        switch (direction.toLowerCase()) {
            case "up":    changed = board.moveUp();    break;
            case "down":  changed = board.moveDown();  break;
            case "left":  changed = board.moveLeft();  break;
            case "right": changed = board.moveRight(); break;
            default:      throw new IllegalArgumentException("Invalid direction: " + direction);
        }
        return new GameState(board);
    }
}
