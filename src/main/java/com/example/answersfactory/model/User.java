package com.example.answersfactory.model;

import com.example.answersfactory.enums.Role;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;
    private String firstName;
    private String lastName;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private int correctAnswers;
    private String description;

    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;

    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Suggestion> suggestions;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "users_badges",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "badge_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<Badge> badges = new HashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_voted_answers",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "answer_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<Answer> votedAnswers = new HashSet<>();

    public void addVotedAnswer(Answer answer){
        votedAnswers.add(answer);
    }

    @OneToMany(mappedBy = "sendUser", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Request> banningRaisedRequests;

    @OneToMany(mappedBy = "receiveUser", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Request> banningReceivedRequests;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
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
