package com.ifsc.secstor.api.repository;

import com.ifsc.secstor.api.model.Role;
import com.ifsc.secstor.api.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    UserModel findByUsername(String username);

    UserModel findByRoleAndUsername(Role role, String username);

    Page<UserModel> findAllByRole(Role role, Pageable pageable);

    boolean existsByUsername(String username);
}
