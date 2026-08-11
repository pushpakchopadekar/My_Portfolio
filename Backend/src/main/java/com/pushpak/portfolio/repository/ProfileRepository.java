package com.pushpak.portfolio.repository;

import com.pushpak.portfolio.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {}
