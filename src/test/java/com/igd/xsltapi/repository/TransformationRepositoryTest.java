package com.igd.xsltapi.repository;

import com.igd.xsltapi.entity.Transformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransformationRepositoryTest {

    @Container
    MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("pass1234");

    @Autowired
    TransformationRepository transformationRepository;

    //    @Test
    void shouldSaveTransformation() {
        // ARRANGE
        Transformation expectedTransformationObject = new Transformation();
        expectedTransformationObject.setContent("content");

        // ACT
        Transformation actualTransformationObject = transformationRepository.save(expectedTransformationObject);

        // ASSERT
        Assertions.assertThat(actualTransformationObject)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedTransformationObject);
    }

}
