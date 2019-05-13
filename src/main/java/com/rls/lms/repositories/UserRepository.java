package com.rls.lms.repositories;

import com.rls.lms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE LOWER(u.company) = LOWER(:company)")
    List<User> findAll(@Param("company") String company);

    @Query("SELECT u FROM User u WHERE LOWER(u.userId) = LOWER(:userId) AND LOWER(u.company) = LOWER(:company)")
    List<User> findByUserId(@Param("userId") String userId, @Param("company") String company);
}
