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

    @Override
    public Mono<Void> deletePlayer(Long id) {
        return playerRepository.deleteById(id);
    }

    @Override
    public Mono<Void> updatePlayerBalance(Long id, Double amount) {
        return playerRepository.findById(id)
                .flatMap(player -> {
                    int newRanking = (int) (player.getRanking() + amount);
                    player.setRanking(newRanking);
                    return playerRepository.save(player);
                })
                .then();
    }

    @Override
    public Flux<Player> getRanking() {
        return playerRepository.findAllByOrderByRankingDesc();
    }

    @Override
    public Mono<Player> updatePlayerName(Long id, String newName) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Player not found with id: " + id)))
                .flatMap(player -> {
                    player.setName(newName);
                    return playerRepository.save(player);
                });
    }
}