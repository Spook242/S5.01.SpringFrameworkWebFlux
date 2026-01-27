package cat.itacademy.blackjack.service;

import cat.itacademy.blackjack.dto.GameCreationRequest;
import cat.itacademy.blackjack.dto.GamePlayRequest;
import cat.itacademy.blackjack.model.mongo.Card;
import cat.itacademy.blackjack.model.mongo.Game;
import cat.itacademy.blackjack.model.mongo.Rank;
import cat.itacademy.blackjack.model.mongo.Suit;
import cat.itacademy.blackjack.repository.mongo.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;

    @Override
    public Mono<Game> getGameById(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Game not found with id: " + id)));
    }

    @Override
    public Mono<Game> createGame(GameCreationRequest request) {
        Game game = new Game();
        game.setStatus("PLAYING");
        game.setPlayerId(1L);
        game.setBet(request.getBet() != null ? request.getBet() : 10.0);

        List<Card> fullDeck = generateDeck();
        Collections.shuffle(fullDeck);

        List<Card> playerCards = new ArrayList<>();
        playerCards.add(fullDeck.remove(0));
        playerCards.add(fullDeck.remove(0));

        List<Card> dealerCards = new ArrayList<>();
        dealerCards.add(fullDeck.remove(0));
        dealerCards.add(fullDeck.remove(0));

        game.setPlayerCards(playerCards);
        game.setDealerCards(dealerCards);
        game.setPlayerScore(calculateScore(playerCards));
        game.setDealerScore(calculateScore(dealerCards));

        return gameRepository.save(game);
    }

    @Override
    public Mono<Game> makeMove(String gameId, GamePlayRequest request) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    if (!"PLAYING".equalsIgnoreCase(game.getStatus())) {
                        return Mono.error(new RuntimeException("Game is already finished"));
                    }

                    List<Card> deck = getAvailableDeck(game);
                    int playerScore = game.getPlayerScore();

                    if ("HIT".equalsIgnoreCase(request.getAction())) {
                        Card drawnCard = deck.remove(0);
                        game.getPlayerCards().add(drawnCard);
                        playerScore = calculateScore(game.getPlayerCards());
                        game.setPlayerScore(playerScore);

                        if (playerScore > 21) {
                            game.setStatus("BUST");
                            return finalizeGame(game, -game.getBet());
                        }
                    }

                    else if ("STAND".equalsIgnoreCase(request.getAction())) {
                        List<Card> dealerCards = game.getDealerCards() != null ?
                                game.getDealerCards() : new ArrayList<>();
                        int dealerScore = calculateScore(dealerCards);

                        while (dealerScore < 17 && !deck.isEmpty()) {
                            dealerCards.add(deck.remove(0));
                            dealerScore = calculateScore(dealerCards);
                        }

                        game.setDealerCards(dealerCards);
                        game.setDealerScore(dealerScore);

                        return determineWinner(game, playerScore, dealerScore);
                    }

                    return gameRepository.save(game);
                });
    }


    private Mono<Game> determineWinner(Game game, int playerScore, int dealerScore) {
        double amount = 0.0;

        if (dealerScore > 21 || playerScore > dealerScore) {
            game.setStatus("PLAYER_WINS");
            amount = game.getBet();
        } else if (playerScore < dealerScore) {
            game.setStatus("DEALER_WINS");
            amount = -game.getBet();
        } else {
            game.setStatus("DRAW");
            amount = 0.0;
        }

        return finalizeGame(game, amount);
    }

    private Mono<Game> finalizeGame(Game game, double amount) {
        if (amount != 0) {
            return playerService.updatePlayerBalance(game.getPlayerId(), amount)
                    .then(gameRepository.save(game));
        }
        return gameRepository.save(game);
    }

    private int calculateScore(List<Card> cards) {
        int score = 0;
        int aceCount = 0;

        for (Card card : cards) {
            switch (card.getRank()) {
                case TWO -> score += 2;
                case THREE -> score += 3;
                case FOUR -> score += 4;
                case FIVE -> score += 5;
                case SIX -> score += 6;
                case SEVEN -> score += 7;
                case EIGHT -> score += 8;
                case NINE -> score += 9;
                case TEN, JACK, QUEEN, KING -> score += 10;
                case ACE -> {
                    score += 11;
                    aceCount++;
                }
            }
        }

        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }
        return score;
    }

    private List<Card> generateDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        return deck;
    }

    private List<Card> getAvailableDeck(Game game) {
        List<Card> fullDeck = generateDeck();
        List<Card> usedCards = new ArrayList<>();
        if (game.getPlayerCards() != null) usedCards.addAll(game.getPlayerCards());
        if (game.getDealerCards() != null) usedCards.addAll(game.getDealerCards());

        return fullDeck.stream()
                .filter(card -> usedCards.stream().noneMatch(used ->
                        used.getRank() == card.getRank() && used.getSuit() == card.getSuit()))
                .collect(Collectors.toList());
    }
}