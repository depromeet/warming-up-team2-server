package com.depromeet.booboo.domain.member;

import com.depromeet.booboo.domain.couple.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderTypeAndProviderUserId(ProviderType providerType, String providerUserId);

    boolean existsByConnectionCode(String connectionCode);

    Optional<Member> findByConnectionCode(String connectionCode);

    List<Member> findByCouple(Couple couple);
}
