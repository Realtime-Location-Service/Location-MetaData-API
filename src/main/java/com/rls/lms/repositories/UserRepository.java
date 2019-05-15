package com.rls.lms.repositories;

import com.rls.lms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE LOWER(u.domain) = LOWER(:domain)")
    List<User> findAll(@Param("domain") String domain);

    @Query("SELECT u FROM User u WHERE LOWER(u.user_id) = LOWER(:user_id) AND LOWER(u.domain) = LOWER(:domain)")
    List<User> findByUser_id(@Param("user_id") String user_id, @Param("domain") String domain);
}
