package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 공통 인터페이스
 * - JavaConfig 설정: 스프링 부트 사용 시 생략 가능(@SpringBootApplication 위치를 지정)
 * - Spring Data JPA가 프록시 객체를 자동으로 생성
 * - @Repository 생략 가능
 *
 * - JpaRepository 인터페이스: 공통 CRUD 제공
 * - 제네릭 <엔티티타입, 식별자타입> 설정
 */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    /** 메서드 이름으로 쿼리 생성 */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /**
     * @Query로 리포지토리 메소드에 쿼리 정의하기, @Param으로 파라미터 바인딩
     * - 실행할 메서드에 정적 쿼리를 직접 작성. App 실행시점에 오류 발견 가능한 장점
     * - 실무에서 많이 사용!
     */
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    /** DTO로 직접 조회 시, JPA의 new 명령어 사용 */
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    /**
     * 컬렉션 파라미터 바인딩 : Collection 타입으로 in절 지원
     * - List말고 Collection으로 받으면 다른 애들도 받을 수 있음
     * - 실무에서 은근 많이 씀!
     */
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    /** 다양한 반환타입 제공 */
    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String name); //단건 Optional

    /** 페이징과 정렬 */
    Page<Member> findByAge(int age, Pageable pageable); //count 쿼리 사용
//    Slice<Member> findByAge(int age, Pageable pageable); //count 쿼리 사용안함
//    List<Member> findByAge(int age, Pageable pageable); //count 쿼리 사용안함
//    List<Member> findByAge(int age, Sort sort);

    /** 성능 최적화를 위해서 count 쿼리를 분리할 수 있음*/
    @Query(value = "select m from Member m",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);

    @Modifying(clearAutomatically = true) //.executeUpdate(); 효과. 없으면 에러남, 옵션 :
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /** @EntityGraph 사용
     * - fetch join의 간편 버전
     * - left outer join 사용
     */
    //공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(String username);

    /** JPA Hint
     * - 더티체킹을 위한 스냅샷을 안만들기 위해 JPA 구현체(Hibernate)에 제공하는 힌트
     * - 조회용으로만 사용, 성능 최적화
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    //forCounting : 반환 타입으로 Page 인터페이스를 적용하면 추가로 호출하는 페이징을 위한 count 쿼리도 쿼리 힌트 적용(기본값 true)
    @QueryHints(value = { @QueryHint(name = "org.hibernate.readOnly", value = "true")}, forCounting = true)
    Page<Member> findByUsername(String name, Pageable pageable);

    /** JPA Lock
     * - JPA가 제공하는 Lock을 어노테이션으로 편리하게 사용
     * - 실시간 트래픽이 많은 서비스에서는 가급적이면 Lock(OPTIMISTIC)을 거는게 좋다
     * - 돈을 맞추는게 중요하면 PESSIMISTIC_WRITE 사용
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String name);

    /** Projections */
    //인터페이스 기반 Projections(구현체에 프록시 객체가 담긴다)
    List<UsernameOnly> findProjectionsByUsername(String username);
    List<NestedClosedProjection> findProjections1ByUsername(String username);

    //클래스 기반 Projections(구현체에 프록시 객체가 담긴다)
    List<UsernameOnlyDto> findProjections2ByUsername(String username);

    //동적 Projections (Generic type을 주면, 동적으로 프로젝션 데이터 번경 가능)
    <T> List<T> findProjections3ByUsername(String username, Class<T> type);

    /**
     * 네이티브 쿼리
     * - 페이징 지원
     * 한계(제약이 너무 많다)
     * - 엔티티를 가져올 때는 데이터를 entity에 맞게 select 절에 다 적어줘야한다, Member 데이터를 다 찍어야한다
     * - 반환 타입이 몇가지 지원 안된다 (보통 엮어서 가져올 때 필요한데 ㅠ)
     * - Sort 파라미터를 통한 정렬이 정상 동작하지 않을 수 있음
     * - 로딩 시점에 문법 확인 불가
     * - 동적 쿼리 불가
     */
    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);

    /**
     * Projections 활용 (New!)
     * 예) 스프링 데이터 JPA 네이티브 쿼리 + 인터페이스 기반 Projections 활용
     * - 안시 SQL 표준 문법
     */
    @Query(value = "SELECT m.member_id as id, m.username, t.name as teamName " +
            "FROM member m left join team t",
            countQuery = "SELECT count(*) from member", //꼭 적어야함
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
