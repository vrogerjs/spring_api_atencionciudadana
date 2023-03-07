package gob.regionancash.atencionciudadano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class AtencionCiudadanaApplication {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private Integer redisPort;

	public static void main(String[] args) {
		SpringApplication.run(AtencionCiudadanaApplication.class, args);
	}

	@Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
		configuration.setUsername("default");
		configuration.setPassword(org.springframework.data.redis.connection.RedisPassword.of("75dQYsXYKR5NLT0Amf56icbrVRRmeF1X"));
        return new JedisConnectionFactory(configuration);
    }
}
