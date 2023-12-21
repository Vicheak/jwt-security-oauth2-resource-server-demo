package com.vicheak.coreapp.security;

import com.vicheak.coreapp.entity.User;
import com.vicheak.coreapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //load user from email and verify by security context
        User user = userRepository.findByEmailAndIsVerifiedTrueAndIsDeletedFalse(username)
                .orElseThrow(
                        () -> {
                            log.error("Email has not been found in the system or is unauthorized!");
                            return new UsernameNotFoundException("Email has not been found in the system or is unauthorized!");
                        }
                );

        log.info("Security email : {}", user.getEmail());
        //log.info("Security username : {}", user.getUsername());

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(user);

        //log.info("Security authorities : {}", customUserDetails.getAuthorities());

        return customUserDetails;
    }

}
