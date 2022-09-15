package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //기본적으로 default 생성자가 있어야 함
@Getter
@Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    protected Member(){} //프록시 생성 시 안막히도록 열어둔다

    public Member(String username) {
        this.username = username;
    }
}