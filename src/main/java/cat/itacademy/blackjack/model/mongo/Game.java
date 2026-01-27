package cat.itacademy.blackjack.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "games")
public class Game {
    @Id
    private String id;
    private Long playerId;
    private List<Card> playerCards;
    private List<Card> dealerCards;
    private List<Card> deck;
    private Integer playerScore;
    private Integer dealerScore;
    private String status;
    private Double bet;
}