package me.security.jwt.config;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // httpServletRequest를 사용하는 요청들에 대한 접근 제한을 설정하겠다는 의미
                .antMatchers("/api/hello", "/").permitAll() // /api/hello에 대한 요청은 인증없이 접근을 허용
                .anyRequest().authenticated(); // 나머지 요청들에 대해서는 인증을 받아야 한다.
    }
}
