package com.usermanagement.repository;

import com.usermanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

  //  Role findByNameIsStartingWithModerator(String name);
  Role findNameById(int id);

  Role findById(int id);

}

