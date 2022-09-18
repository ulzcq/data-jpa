package study.datajpa.repository;

/**
 * 중첩 구조 처리(연관된 것 까지 가져오기)
 * - 프로젝션 대상이 root 엔티티면, JPQL SELECT 절 최적화 가능
 * - 프로젝션 대상이 ROOT가 아니면
 *  - LEFT OUTER JOIN 처리
 *  - 모든 필드를 SELECT해서 엔티티로 조회한 다음에 계산(최적화 X)
 */
public interface NestedClosedProjection {

    String getUsername(); //Member 속성

    TeamInfo getTeam(); //Team

    interface TeamInfo {
        String getName(); //Team 속성
    }
}