package com.rls.lms.repositories;

import com.rls.lms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface UserRepository extends JpaRepository<User, Integer> {

}
