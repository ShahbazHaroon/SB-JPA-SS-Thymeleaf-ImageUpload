package com.ubaidsample.SBJPASSThymeleafImageUpload.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ubaidsample.SBJPASSThymeleafImageUpload.repository.UserRepository;

@Component
public class UserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailService(UserRepository siteUserRepository) {
        this.userRepository = siteUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return userRepository.findByName(name)
                .map(user -> new User(user.getName(), user.getPassword(), user.isEnabled(),
                        user.isEnabled(), user.isEnabled(), user.isEnabled(),
                        AuthorityUtils.createAuthorityList("USER")
                ))
                .orElseThrow(() -> new UsernameNotFoundException("can't find"));
    }
}
