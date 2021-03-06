package diplom.problembook.config;

import diplom.problembook.models.User;
import diplom.problembook.repositories.UserRepository;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/js/**", "/error**", "/images/**", "/api/mobile/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .and()
                .csrf().disable();
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserRepository userRepository){
        return map -> {
            String id = (String) map.get("sub");
            User user = userRepository.findById(id).orElseGet(() -> {
                User newUser = new User();

                newUser.setId(id);
                newUser.setEmail((String) map.get("email"));
                newUser.setName((String) map.get("name"));
                newUser.setGender((String) map.get("gender"));
                newUser.setLocale((String) map.get("locale"));
                newUser.setImage((String) map.get("picture"));
                return newUser;
            });

            user.setLastVisit(LocalDateTime.now());
            return userRepository.save(user);
        };
    }
}
