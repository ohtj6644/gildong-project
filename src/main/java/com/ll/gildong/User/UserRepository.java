package com.ll.gildong.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {


    Optional<SiteUser> findByusername(String username);
}