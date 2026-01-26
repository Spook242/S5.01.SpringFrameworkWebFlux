package cat.itacademy.blackjack.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "cat.itacademy.blackjack.repository.mysql")
@EnableReactiveMongoRepositories(basePackages = "cat.itacademy.blackjack.repository.mongo")
public class PersistenceConfig { }