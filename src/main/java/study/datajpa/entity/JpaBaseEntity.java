package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass //진짜 상속관게는 아니고, 속성만 내려서 테이블에서 같이 쓸 수 있음
@Getter
public class JpaBaseEntity {

    @Column(updatable = false) //값 변경 안되게
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now; //값 같이 맞춰놔야 나주에 쿼리 날릴 때 편함
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
