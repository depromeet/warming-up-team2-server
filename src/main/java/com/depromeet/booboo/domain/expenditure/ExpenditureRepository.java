package com.depromeet.booboo.domain.expenditure;

import com.depromeet.booboo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
    Optional<Expenditure> findByMemberAndExpenditureId(Member member, Long expenditureId);
}
