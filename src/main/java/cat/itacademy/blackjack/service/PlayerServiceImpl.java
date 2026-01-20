package cat.itacademy.blackjack.service;

import cat.itacademy.blackjack.model.mysql.Player;
import cat.itacademy.blackjack.repository.mysql.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public Flux<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Mono<Player> createPlayer(String name) {
        Player newPlayer = new Player(null, name, 0);
        return playerRepository.save(newPlayer);
    }
}