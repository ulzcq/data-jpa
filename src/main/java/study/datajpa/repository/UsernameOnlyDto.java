package study.datajpa.repository;

/**
 * 클래스 기반 Projection
 * - 생성자의 파라미터 이름으로 매칭
 */
public class UsernameOnlyDto {

    private final String username;

    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}