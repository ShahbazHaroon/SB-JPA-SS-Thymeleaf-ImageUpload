package com.ubaidsample.SBJPASSThymeleafImageUpload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ubaidsample.SBJPASSThymeleafImageUpload.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
}
