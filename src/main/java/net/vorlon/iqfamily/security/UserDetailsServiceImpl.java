package net.vorlon.iqfamily.security;

import lombok.extern.slf4j.Slf4j;
import net.vorlon.iqfamily.config.SpringSecurityConfig;
import net.vorlon.iqfamily.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final SpringSecurityConfig authModuleConfig;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, SpringSecurityConfig authModuleConfig) {
        this.userRepository = userRepository;
        this.authModuleConfig=authModuleConfig;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<net.vorlon.iqfamily.model.domain.User> userList = userRepository.findByEmail(username);
//        String encoded123=authModuleConfig.passwordEncoder().encode("12345678");

        if (userList.size()==0) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        String encodedPswd=authModuleConfig.passwordEncoder().encode(userList.get(0).getPassword());
        List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(userList.get(0).getName())
                .password(encodedPswd)
                .authorities(authorities)
                .build();
        return userDetails;
    }
}
