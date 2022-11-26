package com.example.answersfactory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;

@Entity
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private int correctAnswers;
    private String description;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Question> questions;

    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Answer> answers;

    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, fetch = FetchType.EAGER )
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Suggestion> suggestions;

    @OneToMany(mappedBy = "sendUser", cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Request> banningRaisedRequests;

    @OneToMany(mappedBy = "receiveUser", cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Request> banningReceivedRequests;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "users_badges",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "badge_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<Badge> badges = new HashSet<>();

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
