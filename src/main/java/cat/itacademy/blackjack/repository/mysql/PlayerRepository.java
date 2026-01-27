package cat.itacademy.blackjack.repository.mysql;

import cat.itacademy.blackjack.model.mysql.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {

    Mono<Player> findByName(String name);

    Flux<Player> findAllByOrderByRankingDesc();
}