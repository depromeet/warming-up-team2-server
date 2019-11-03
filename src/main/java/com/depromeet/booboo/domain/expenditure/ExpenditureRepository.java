package com.depromeet.booboo.domain.expenditure;

import com.depromeet.booboo.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
    Optional<Expenditure> findByMemberAndExpenditureId(Member member, Long expenditureId);

    Page<Expenditure> findByMemberIn(List<Member> members, Pageable pageable);

    Page<Expenditure> findByMemberInAndCategoryId(List<Member> members, Long categoryId, Pageable pageable);
}
