package com.ai.security;

import com.ai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("MyUserDetailsServiceImpl")
public class AppUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var result = repo.findOneByLoginId(username)
                .map(user -> User.withUsername(username)
                        .password(user.getPassword())
                        .authorities(AuthorityUtils.createAuthorityList(user.getRole().name()))
                        .disabled(!user.isActive())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with login id %s".formatted(username)));
        return result;
    }

}