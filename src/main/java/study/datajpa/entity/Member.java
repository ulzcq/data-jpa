package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity //기본적으로 default 생성자가 있어야 함
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"}) //출력하기 편하려고 넣음(*주의 연관관계 필드는 가급적 넣지X, 무한루프..)
public class Member extends JpaBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id") //조인할 때 편함, 관례!
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this(username, 0);
    }

    public Member(String username, int age) {
        this(username, age, null);
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    /** 연관관계 편의 메서드*/
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}