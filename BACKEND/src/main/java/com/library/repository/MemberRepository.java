package com.library.repository;

import com.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    @Query("SELECT m FROM Member m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%',:k,'%')) OR LOWER(m.email) LIKE LOWER(CONCAT('%',:k,'%'))")
    List<Member> search(@Param("k") String keyword);
}
