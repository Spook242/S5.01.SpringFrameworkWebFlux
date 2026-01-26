package cat.itacademy.blackjack.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GamePlayRequest {
    private String action;
}