package cat.itacademy.blackjack.service;

import cat.itacademy.blackjack.dto.GameCreationRequest;
import cat.itacademy.blackjack.dto.GamePlayRequest;
import cat.itacademy.blackjack.model.mongo.Game;
import reactor.core.publisher.Mono;

public interface GameService {
    Mono<Game> createGame(GameCreationRequest request);
    Mono<Game> makeMove(String gameId, GamePlayRequest request);
    Mono<Game> getGameById(String id);
    Mono<Void> deleteGame(String id);
}