package com.rls.lms.repositories;

import com.rls.lms.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>, ExtendedUserRepository {
//    @Query(value = "INSERT INTO `user` (user_id, email) VALUES (‘Jo’, ‘jo@email.com’)\n ON DUPLICATE KEY UPDATE email = ‘jo@email.com’", nativeQuery = true)
//    User save(@Param("user") User user);

    @Query("SELECT u FROM User u WHERE LOWER(u.domain) = LOWER(:domain)")
    List<User> findAll(@Param("domain") String domain, Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.domain) = LOWER(:domain) AND LOWER(u.status) = LOWER(:status)")
    List<User> findByStatus(@Param("domain") String domain, @Param("status") String Status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.user_id) = LOWER(:userId) AND LOWER(u.domain) = LOWER(:domain)")
    List<User> findByUserId(@Param("userId") String userId, @Param("domain") String domain);

    @Query("SELECT u FROM User u WHERE u.user_id IN (:userIds) AND LOWER(u.domain) = LOWER(:domain)")
    List<User> find(String domain, String[] userIds);
}
