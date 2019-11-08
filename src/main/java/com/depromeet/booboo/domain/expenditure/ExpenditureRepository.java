package com.depromeet.booboo.domain.expenditure;

import com.depromeet.booboo.domain.category.Category;
import com.depromeet.booboo.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
    Optional<Expenditure> findByMemberAndExpenditureId(Member member, Long expenditureId);

    List<Expenditure> findByMemberIn(List<Member> members);

    List<Expenditure> findByMemberInAndExpendedAtGreaterThanEqual(List<Member> members, LocalDate expendedAt);

    Page<Expenditure> findByMemberIn(List<Member> members, Pageable pageable);

    Page<Expenditure> findByMemberInAndCategory(List<Member> members, Category category, Pageable pageable);
}
