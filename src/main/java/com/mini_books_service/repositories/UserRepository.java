package com.mini_books_service.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.mini_books_service.models.User.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
