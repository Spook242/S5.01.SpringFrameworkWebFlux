package cat.itacademy.blackjack.controller;

import cat.itacademy.blackjack.model.mysql.Player;
import cat.itacademy.blackjack.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlayerService playerService;

    @Test
    void getAllPlayers_ReturnsOkAndListOfPlayers() {
        Player player1 = new Player(1L, "Marc", 100);

        when(playerService.getAllPlayers()).thenReturn(Flux.just(player1));

        webTestClient.get()
                .uri("/players")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Player.class)
                .hasSize(1)
                .contains(player1);
    }

    @Test
    void createPlayer_ReturnsOkAndNewPlayer() {
        Player playerResponse = new Player(1L, "Jordi", 0);

        when(playerService.createPlayer("Jordi")).thenReturn(Mono.just(playerResponse));

        webTestClient.post()
                .uri("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Player(null, "Jordi", 0))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Jordi")
                .jsonPath("$.ranking").isEqualTo(0);
    }
}