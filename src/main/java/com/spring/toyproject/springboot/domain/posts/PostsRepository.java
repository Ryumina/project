package com.spring.toyproject.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Posts 클래스로 Database를 접근하게 해줄 JpaRepository 인터페이스
 * ibatis 또는 MyBatis등에서 Dao라고 불리는 DB Layer
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {


}
