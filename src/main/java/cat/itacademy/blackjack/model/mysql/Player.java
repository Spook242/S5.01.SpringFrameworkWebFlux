package cat.itacademy.blackjack.model.mysql;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("players")
public class Player {

    @Id
    private Long id;

    private String name;
    private Integer ranking;
}