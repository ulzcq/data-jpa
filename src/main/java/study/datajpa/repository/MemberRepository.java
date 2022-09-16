package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

/**
 * 공통 인터페이스
 * - JavaConfig 설정: 스프링 부트 사용 시 생략 가능(@SpringBootApplication 위치를 지정)
 * - Spring Data JPA가 프록시 객체를 자동으로 생성
 * - @Repository 생략 가능
 *
 * - JpaRepository 인터페이스: 공통 CRUD 제공
 * - 제네릭 <엔티티타입, 식별자타입> 설정
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
}
