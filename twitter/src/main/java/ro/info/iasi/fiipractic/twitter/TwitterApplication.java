package ro.info.iasi.fiipractic.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;

@SpringBootApplication
public class TwitterApplication {


	public static void main(String[] args) {
		SpringApplication.run(TwitterApplication.class, args);
	}
}
