package com.vicheak.coreapp.init;

import com.vicheak.coreapp.entity.Authority;
import com.vicheak.coreapp.entity.Role;
import com.vicheak.coreapp.repository.AuthorityRepository;
import com.vicheak.coreapp.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;

    //@PostConstruct
    public void setUpInitialization() {
        //set up authorities
        Authority studentRead = Authority.builder()
                .name("student:read")
                .build();

        Authority studentWrite = Authority.builder()
                .name("student:write")
                .build();

        Authority studentUpdate = Authority.builder()
                .name("student:update")
                .build();

        Authority studentDelete = Authority.builder()
                .name("student:delete")
                .build();

        Authority courseRead = Authority.builder()
                .name("course:read")
                .build();

        Authority courseWrite = Authority.builder()
                .name("course:write")
                .build();

        Authority courseUpdate = Authority.builder()
                .name("course:update")
                .build();

        Authority courseDelete = Authority.builder()
                .name("course:delete")
                .build();

        Authority ratingRead = Authority.builder()
                .name("rating:read")
                .build();

        Authority ratingWrite = Authority.builder()
                .name("rating:write")
                .build();

        Authority ratingUpdate = Authority.builder()
                .name("rating:update")
                .build();

        Authority ratingDelete = Authority.builder()
                .name("rating:delete")
                .build();

        Set<Authority> studentAuthorities = Set.of(studentRead,
                studentWrite, studentUpdate, studentDelete);

        Set<Authority> courseAuthorities = Set.of(courseRead,
                courseWrite, courseUpdate, courseDelete);

        Set<Authority> ratingAuthorities = Set.of(ratingRead,
                ratingWrite, ratingUpdate, ratingDelete);

        Set<Authority> fullAuthorities = new HashSet<>() {{
            addAll(studentAuthorities);
            addAll(courseAuthorities);
            addAll(ratingAuthorities);
        }};

        authorityRepository.saveAll(fullAuthorities);

        //set up roles
        Role admin = Role.builder()
                .name("ADMIN")
                .authorities(fullAuthorities)
                .build();

        Role staff = Role.builder()
                .name("STAFF")
                .authorities(new HashSet<>() {{
                    addAll(studentAuthorities);
                    add(courseRead);
                }})
                .build();

        roleRepository.saveAll(Set.of(admin, staff));
    }

}
