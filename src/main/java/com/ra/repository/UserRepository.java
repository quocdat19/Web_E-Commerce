package com.ra.repository;

import com.ra.model.entity.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    @Query("SELECT u from Users u WHERE u.fullName like %?1% ")
    List<Users> searchAllByUAndFullName(String keyword);
    boolean existsByUsername(String username);
}
