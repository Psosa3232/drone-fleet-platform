package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}