package sbe.web1.security;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoginUserDetail extends User {
    private static final long serialVersionUID = 1L;

    String nickname;

    int age;

    Date birthDay;

    public LoginUserDetail(String username, String password, String nickname, int age, Date birthDay, boolean enabled,
            boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.nickname = nickname;
        this.age = age;
        this.birthDay = birthDay;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "LoginUserDetail [nickname=" + nickname + ", age=" + age + ", birthDay=" + birthDay + ", super.toString()=" + super.toString() + "]";
    }
}
