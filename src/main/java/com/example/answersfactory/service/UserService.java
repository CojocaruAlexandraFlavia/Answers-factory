package com.example.answersfactory.service;

import com.example.answersfactory.model.dto.RegisterUserRequest;
import com.example.answersfactory.model.User;
import com.example.answersfactory.model.dto.UserDto;
import com.example.answersfactory.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static com.example.answersfactory.model.dto.UserDto.convertEntityToDto;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
        }
        return new org.springframework.security.core.userdetails.User("", "", new ArrayList<>());
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public UserDto registerUser(@NotNull RegisterUserRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDescription(request.getDescription());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        return convertEntityToDto(userRepository.save(user));
    }

    public Optional<User> findUserById(Long id){
        return userRepository.findById(id);
    }

    public void insertUser(User user){
        userRepository.save(user);
    }
}
