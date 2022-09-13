package com.io.realworld.repository;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Table(name = "users")
@Entity
@Getter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;
    private String password;

    private String bio;
    private String image;

    @Builder
    public User(String username, String email, String password, String bio, String image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.image = image;
    }

    protected User(){}
    public static User of(String username, String email, String password) {
        return new User(username, email, password, "", "");
    }

    @Override
    public String getUsername(){
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
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
