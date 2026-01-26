package cat.itacademy.blackjack.service;

import cat.itacademy.blackjack.dto.GameCreationRequest;
import cat.itacademy.blackjack.dto.GamePlayRequest;
import cat.itacademy.blackjack.model.mongo.Card;
import cat.itacademy.blackjack.model.mongo.Game;
import cat.itacademy.blackjack.model.mongo.Rank;
import cat.itacademy.blackjack.model.mongo.Suit;
import cat.itacademy.blackjack.repository.mongo.GameRepository;
import cat.itacademy.blackjack.repository.mysql.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Override
    public Mono<Game> createGame(GameCreationRequest request) {
        return playerRepository.findByName(request.getPlayerName())
                .switchIfEmpty(Mono.error(new RuntimeException("Player not found: " + request.getPlayerName())))
                .flatMap(player -> {
                    Game game = new Game();
                    game.setPlayerId(player.getId());
                    game.setStatus("ACTIVE");

                    List<Card> deck = generateDeck();
                    List<Card> initialCards = new ArrayList<>();
                    initialCards.add(deck.remove(0));
                    initialCards.add(deck.remove(0));

                    game.setPlayerCards(initialCards);

                    int score = calculateScore(initialCards);
                    game.setPlayerScore(score);

                    return gameRepository.save(game);
                });
    }


    private List<Card> generateDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    private List<Card> getAvailableDeck(Game game) {
        List<Card> fullDeck = generateDeck();

        if (game.getPlayerCards() != null) {
            fullDeck.removeAll(game.getPlayerCards());
        }

        if (game.getDealerCards() != null) {
            fullDeck.removeAll(game.getDealerCards());
        }

        return fullDeck;
    }

    private int calculateScore(List<Card> cards) {
        int totalScore = 0;
        int aceCount = 0;

        for (Card card : cards) {
            totalScore += card.getRank().getValue();
            if (card.getRank() == Rank.ACE) {
                aceCount++;
            }
        }

        while (totalScore > 21 && aceCount > 0) {
            totalScore -= 10;
            aceCount--;
        }

        return totalScore;
    }


    @Override
    public Mono<Game> makeMove(String gameId, GamePlayRequest request) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new RuntimeException("Game not found")))
                .flatMap(game -> {
                    if (!"ACTIVE".equals(game.getStatus())) {
                        return Mono.error(new RuntimeException("Game is already finished"));
                    }

                    List<Card> deck = getAvailableDeck(game);

                    if ("HIT".equalsIgnoreCase(request.getAction())) {
                        game.getPlayerCards().add(deck.remove(0));

                        int newScore = calculateScore(game.getPlayerCards());
                        game.setPlayerScore(newScore);

                        if (newScore > 21) {
                            game.setStatus("BUST");
                        }

                    } else if ("STAND".equalsIgnoreCase(request.getAction())) {
                        List<Card> dealerCards = game.getDealerCards() != null ?
                                game.getDealerCards() : new ArrayList<>();

                        int dealerScore = calculateScore(dealerCards);

                        while (dealerScore < 17) {
                            deck = getAvailableDeck(game);
                            dealerCards.add(deck.remove(0));
                            dealerScore = calculateScore(dealerCards);
                        }

                        game.setDealerCards(dealerCards);
                        game.setDealerScore(dealerScore);

                        if (dealerScore > 21) {
                            game.setStatus("PLAYER_WINS");
                        } else if (dealerScore > game.getPlayerScore()) {
                            game.setStatus("DEALER_WINS");
                        } else if (dealerScore < game.getPlayerScore()) {
                            game.setStatus("PLAYER_WINS");
                        } else {
                            game.setStatus("DRAW");
                        }
                    }

                    return gameRepository.save(game);
                });


    }
    @Override
    public Mono<Game> getGameById(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Game not found with id: " + id)));
    }

}