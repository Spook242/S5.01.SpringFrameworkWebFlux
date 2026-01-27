package cat.itacademy.blackjack.controller;

import cat.itacademy.blackjack.model.mysql.Player;
import cat.itacademy.blackjack.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public Flux<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @PostMapping
    public Mono<Player> createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public Mono<Void> deletePlayer(@PathVariable Long id) {
        return playerService.deletePlayer(id);
    }

    @GetMapping("/ranking")
    public Flux<Player> getRanking() {
        return playerService.getRanking();
    }
}