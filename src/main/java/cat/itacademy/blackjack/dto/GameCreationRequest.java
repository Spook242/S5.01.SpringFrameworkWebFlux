package cat.itacademy.blackjack.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameCreationRequest {
    private String playerName;
    private Double bet;
}