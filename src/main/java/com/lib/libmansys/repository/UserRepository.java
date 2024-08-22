package com.lib.libmansys.repository;

import com.lib.libmansys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}