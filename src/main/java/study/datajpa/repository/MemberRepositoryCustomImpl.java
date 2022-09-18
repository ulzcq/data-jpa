package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 사용자 정의 구현 클래스
 * - 규칙: '사용자 정의 인터페이스명 + Impl'(권장) 혹은 '리포지토리 인터페이스명 + Impl'
 * - 스프링 데이터 JPA가 인식해서 스프링 빈으로 등록
 * - 실무에서는 주로 QueryDSL이나 SpringJdbcTemplate을 함께 사용할 때 이 기능 자주 사용
 */
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}