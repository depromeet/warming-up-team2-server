package com.depromeet.booboo.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderTypeAndProviderUserId(ProviderType providerType, String providerUserId);

    boolean existsByConnectionCode(String connectionCode);
}
