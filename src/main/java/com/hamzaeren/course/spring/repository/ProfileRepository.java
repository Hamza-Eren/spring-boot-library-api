package com.hamzaeren.course.spring.repository;

import com.hamzaeren.course.spring.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // Profile → user → username alanına göre arama yapar
    Optional<Profile> findByUserUsername(String username);
}
