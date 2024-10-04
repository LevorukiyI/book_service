package com.modsensoftware.book_service.models;

import com.modsensoftware.book_service.authorities.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private JwtRefreshToken jwtRefreshToken;

    @Builder.Default
    @Column(name = "blocked", nullable = false)
    private Boolean blocked = false;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column
    private UserRole userRole = UserRole.USER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userRole.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
