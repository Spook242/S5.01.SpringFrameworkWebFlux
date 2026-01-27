package cat.itacademy.blackjack.controller;

import cat.itacademy.blackjack.dto.GameCreationRequest;
import cat.itacademy.blackjack.dto.GamePlayRequest;
import cat.itacademy.blackjack.model.mongo.Game;
import cat.itacademy.blackjack.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Game> createGame(@RequestBody GameCreationRequest request) {
        return gameService.createGame(request);
    }

    @PostMapping("/{id}/play")
    public Mono<Game> makeMove(@PathVariable String id, @RequestBody GamePlayRequest request) {
        return gameService.makeMove(id, request);
    }

    @GetMapping("/{id}")
    public Mono<Game> getGameById(@PathVariable String id) {
        return gameService.getGameById(id);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id);
    }
}