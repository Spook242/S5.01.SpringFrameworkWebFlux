package cat.itacademy.blackjack.service;

import cat.itacademy.blackjack.model.mysql.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {
    Flux<Player> getAllPlayers();
    Mono<Player> createPlayer(String name);
    Mono<Void> deletePlayer(Long id);
}