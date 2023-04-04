package com.jafui.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/*import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
*/

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
*/
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.jafui.app.entidades.User;
import com.jafui.app.persistencia.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { PropertyPlaceholderAutoConfiguration.class, UserTests.DynamoDBConfig.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class UserTests {
    // private static Logger LOGGER = LoggerFactory.getLogger(UserTests.class);
    // private SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");

    @Configuration
    @EnableDynamoDBRepositories(basePackageClasses = { UserRepository.class })
    public static class DynamoDBConfig {

        @Value("${amazon.aws.accesskey}")
        private String amazonAWSAccessKey;

        @Value("${amazon.aws.secretkey}")
        private String amazonAWSSecretKey;

        public AWSCredentialsProvider amazonAWSCredentialsProvider() {
            return new AWSStaticCredentialsProvider(amazonAWSCredentials());
        }

        @Bean
        public AWSCredentials amazonAWSCredentials() {
            return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
        }

        @Bean
        public AmazonDynamoDB amazonDynamoDB() {
            return AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
                    .withRegion(Regions.US_EAST_1).build();
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = new User("1", "João", "joao@gmail.com", "123456");
        userRepository.save(user);

        User savedUser = userRepository.findById("1").orElse(null);
        assertNotNull(savedUser);
        assertEquals(user, savedUser);
    }

    @Test
    public void testUpdateUser() {
        User user = new User("1", "João", "joao@gmail.com", "123456");
        userRepository.save(user);

        user.setName("José");
        userRepository.save(user);

        User updatedUser = userRepository.findById("1").orElse(null);
        assertNotNull(updatedUser);
        assertEquals("José", updatedUser.getName());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("1", "João", "joao@gmail.com", "123456");
        userRepository.save(user);

        userRepository.deleteById("1");

        User deletedUser = userRepository.findById("1").orElse(null);
        assertNull(deletedUser);
    }

    @Test
    public void testFindAllUsers() {
        User user1 = new User("1", "João", "joao@gmail.com", "123456");
        userRepository.save(user1);

        User user2 = new User("2", "Maria", "maria@gmail.com", "654321");
        userRepository.save(user2);

        // Iterable<User> users = userRepository.findAll();

        /*
         * int count = 0;
         * for (User user : users) {
         * count++;
         * }
         */

        // assertEquals(2, count);
    }

    @Test
    public void testFindByEmail() {
        User user = new User("1", "João", "joao@gmail.com", "123456");
        userRepository.save(user);

        User foundUser = userRepository.findByEmail("joao@gmail.com");
        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

}
