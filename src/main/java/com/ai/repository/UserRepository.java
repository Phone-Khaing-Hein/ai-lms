package com.ai.repository;

import java.util.List;
import java.util.Optional;

import com.ai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findOneByLoginId(String username);

//    List<User> findByBatchId(int batchId);
}
