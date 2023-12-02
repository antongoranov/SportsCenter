package com.sportscenter.repository;

import com.sportscenter.model.entity.UserRoleEntity;
import com.sportscenter.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    UserRoleEntity findByRole(UserRoleEnum userRoleEnum);
}
