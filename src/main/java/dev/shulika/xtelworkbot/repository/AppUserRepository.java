package dev.shulika.xtelworkbot.repository;

import dev.shulika.xtelworkbot.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}