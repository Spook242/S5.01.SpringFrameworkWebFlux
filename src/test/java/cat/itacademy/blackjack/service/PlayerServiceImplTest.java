package cat.itacademy.blackjack.service;

import cat.itacademy.blackjack.model.mysql.Player;
import cat.itacademy.blackjack.repository.mysql.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Test
    void getAllPlayers_ReturnsFluxOfPlayers() {
        Player player1 = new Player(1L, "Marc", 100);
        Player player2 = new Player(2L, "Laura", 200);
        when(playerRepository.findAll()).thenReturn(Flux.just(player1, player2));

        Flux<Player> result = playerService.getAllPlayers();

        StepVerifier.create(result)
                .expectNext(player1)
                .expectNext(player2)
                .verifyComplete();
    }

    @Test
    void createPlayer_ReturnsMonoWithSavedPlayer() {
        String newName = "Jordi";
        Player savedPlayer = new Player(3L, newName, 0);

        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(savedPlayer));

        Mono<Player> result = playerService.createPlayer(newName);

        StepVerifier.create(result)
                .expectNextMatches(player -> player.getId().equals(3L) && player.getName().equals(newName))
                .verifyComplete();
    }

    @Test
    void deletePlayer_ReturnsEmptyMono() {
        Long id = 1L;
        when(playerRepository.deleteById(id)).thenReturn(Mono.empty());

        Mono<Void> result = playerService.deletePlayer(id);

        StepVerifier.create(result)
                .verifyComplete();
    }
}