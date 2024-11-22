package com.modsensoftware.book_service.security.models.authorities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum Role {
    USER(
            Set.of(
                    Permission.GET_ALL_BOOKS,
                    Permission.GET_BOOK_BY_ID,
                    Permission.GET_BOOK_BY_ISBN
            )
    ),
    LIBRARY_WORKER(
            Set.of(
                    Permission.GET_ALL_BOOKS,
                    Permission.GET_BOOK_BY_ID,
                    Permission.GET_BOOK_BY_ISBN,
                    Permission.ADD_BOOK,
                    Permission.EDIT_BOOK,
                    Permission.DELETE_BOOK_BY_ID,
                    Permission.DELETE_BOOK_BY_ISBN
            )
    ),
    SECRET_KEY(
            Set.of(
                    Permission.GET_ALL_BOOKS,
                    Permission.GET_BOOK_BY_ID,
                    Permission.GET_BOOK_BY_ISBN,
                    Permission.ADD_BOOK,
                    Permission.EDIT_BOOK,
                    Permission.DELETE_BOOK_BY_ID,
                    Permission.DELETE_BOOK_BY_ISBN
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority("PERMISSION_"+ permission.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}