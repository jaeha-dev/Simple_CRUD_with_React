package com.github.devsjh.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @title  : User 레포지토리 클래스
 * @author : jaeha-dev (Git)
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
}