package com.igd.xsltapi.repository;

import com.igd.xsltapi.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryEmbeddedTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldSaveUser() {
        // ARRANGE
        User user = new User();
        user.setName("user");
        user.setUsername("username");
        user.setEmail("user@site.com");
        user.setPassword("pass1234");

        // ACT
        User savedUser = userRepository.save(user);

        // ASSERT
        Assertions.assertThat(savedUser).usingRecursiveComparison().ignoringFields("id").isEqualTo(user);
    }

    @Test
    @Sql("classpath:test-data.sql")
    public void shouldSaveUsersThroughSqlFile() {
        Optional<User> test = userRepository.findByUsername("username");
        Assertions.assertThat(test).isNotEmpty();
    }

}
