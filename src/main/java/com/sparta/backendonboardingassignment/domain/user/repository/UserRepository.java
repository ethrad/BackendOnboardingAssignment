package com.sparta.backendonboardingassignment.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.backendonboardingassignment.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
