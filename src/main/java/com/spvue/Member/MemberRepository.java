package com.spvue.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);
    boolean existsByDisplayName(String displayName);

    Optional<Member> findByUsername(String username);
}
