package com.example.game2048.controller;

import com.example.game2048.model.GameState;
import com.example.game2048.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/state")
    public GameState getState() {
        return gameService.getState();
    }

    @PostMapping("/reset")
    public GameState reset() {
        return gameService.reset();
    }

    @PostMapping("/move")
    public ResponseEntity<?> move(@RequestParam String dir) {
        try {
            GameState state = gameService.move(dir);
            return ResponseEntity.ok(state);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
