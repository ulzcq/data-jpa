package study.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * 저장시점에 등록일, 등록자는 물론이고 수정일, 수정자도 같은 데이터가 저장된다. 데이터가 중복
 * 저장되는 것 같지만, 이렇게 해두면 변경 컬럼만 확인해도 마지막에 업데이트한 유저를 확인 할 수 있으므로
 * 유지보수 관점에서 편리하다. 이렇게 하지 않으면 변경 컬럼이 null 일때 등록 컬럼을 또 찾아야 한다.
 *
 * > 참고로 저장시점에 저장데이터만 입력하고 싶으면 @EnableJpaAuditing(modifyOnCreate = false) 옵션을 사용하면 된다.
 */
@EntityListeners(AuditingEntityListener.class) //이벤트 기반 동작
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false) //수정 안되도록
    private String createdBy; //등록자

    @LastModifiedBy
    private String lastModifiedBy; //수정자
}
