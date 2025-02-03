package com.mini_books_service.services;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mini_books_service.models.User.User;
import com.mini_books_service.models.User.UserDTO;
import com.mini_books_service.models.User.UserViewModel;
import com.mini_books_service.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UserDTO createNewUser(UserViewModel userView) {
        String passwordHash = passwordEncoder.encode(userView.getPassword());

        User userEntity = new User();
        userEntity.setName(userView.getName());
        userEntity.setEmail(userView.getEmail());
        userEntity.setPasswordHash(passwordHash);
        
        return new UserDTO(userRepository.save(userEntity));
    }

    public UserDTO login(UserViewModel userView) {
        Optional<User> optionalUser = userRepository.findByEmail(userView.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Boolean userAuthenticated = passwordEncoder.matches(userView.getPassword(), user.getPasswordHash());

            if (userAuthenticated) {
                return new UserDTO(user);
            }
        }

        return null;
    }

    public UserDTO updateUser(UserViewModel userView) {
        Long userId = Long.parseLong(new String(Base64.getDecoder().decode(userView.getIdHash())));
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User userEntity = optionalUser.get();
            Boolean passwordMatches = passwordEncoder.matches(userView.getPassword(), userEntity.getPasswordHash());

            if (passwordMatches) {
                userEntity.setName(userView.getName());
                userEntity.setEmail(userView.getEmail());
            }
            else {
                String passwordHash = passwordEncoder.encode(userView.getPassword());

                userEntity.setName(userView.getName());
                userEntity.setEmail(userView.getEmail());
                userEntity.setPasswordHash(passwordHash);
            }

            return new UserDTO(userRepository.save(userEntity));
        }

        return null;
    }

    public void deleteUser(String idHash) {
        Long userId = Long.parseLong(new String(Base64.getDecoder().decode(idHash)));

        userRepository.deleteById(userId);
    }
}
