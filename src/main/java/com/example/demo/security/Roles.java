package com.example.demo.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.security.Permissions.*;

public enum Roles {

    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
    STUDENT(Sets.newHashSet(COURSE_READ, STUDENT_READ));

    private final Set<Permissions> permissions;

    Roles(Set<Permissions> permissions){
        this.permissions = permissions;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities(){
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission())).collect(Collectors.toSet());
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return simpleGrantedAuthorities;
    }
}
