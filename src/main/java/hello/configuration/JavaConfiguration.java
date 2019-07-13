package hello.configuration;

import hello.mapper.UserMapper;
import hello.service.OrderService;
import hello.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class JavaConfiguration {

    @Bean
    public OrderService orderService() {
        return new OrderService();
    }

    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder){
        return new UserService(bCryptPasswordEncoder);
    }
}
