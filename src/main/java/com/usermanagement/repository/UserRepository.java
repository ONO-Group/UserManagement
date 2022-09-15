package com.usermanagement.repository;

import com.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmailAndPassword( String email, String password);
    User findById(int id);

    boolean findByEmail(String email);

   List<User> findAllByCreatedBy(User createdBy);
}
