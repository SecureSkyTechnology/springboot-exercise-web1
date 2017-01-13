package sbe.web1.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUserDetailsService implements UserDetailsService {
    static final Logger LOG = LoggerFactory.getLogger(LoginUserDetailsService.class);

    @Autowired
    JdbcTemplate jt;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("<****> LoginUserDetailsService : accepted username = {}", username);
        List<String> authorities = jt.query("select authority from login_authorities where username = ?",
                new Object[] { username }, new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("authority");
                    }
                });
        List<GrantedAuthority> ga = AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));
        LOG.info("<****> LoginUserDetailsService : GrantedAuthorities by username = {} : {}", username, ga);
        try {
            LoginUserDetail lud = jt.queryForObject("select * from login_user where username = ?",
                    new Object[] { username }, new RowMapper<LoginUserDetail>() {
                        @Override
                        public LoginUserDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
                            return new LoginUserDetail(
                                    rs.getString("username"),
                                    rs.getString("password"),
                                    rs.getString("nickname"),
                                    rs.getInt("age"),
                                    rs.getDate("birthday"),
                                    !rs.getBoolean("disabled"),
                                    !rs.getBoolean("user_expired"),
                                    !rs.getBoolean("pass_expired"),
                                    !rs.getBoolean("user_locked"), ga);
                        }
                    });
            LOG.info("<****> LoginUserDetailsService : UserDetails by username = {} : {}", username, lud);
            return lud;
        } catch (Exception e) {
            LOG.warn("loadUserByUsername():", e);
            throw new UsernameNotFoundException("user[" + username + "] not found.");
        }
    }

    public void updatePassword(String username, String newRawPassword, PasswordEncoder pe) {
        jt.update("update login_user set password = ? where username = ?", pe.encode(newRawPassword), username);
    }
}
