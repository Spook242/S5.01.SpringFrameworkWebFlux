package cat.itacademy.blackjack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "cat.itacademy.blackjack.repository.mysql")
@EnableReactiveMongoRepositories(basePackages = "cat.itacademy.blackjack.repository.mongo")
public class BlackjackApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlackjackApiApplication.class, args);
    }
}