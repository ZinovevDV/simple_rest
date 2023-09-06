package ru.mail.zinovev_dv.simple_rest.Security.service;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mail.zinovev_dv.simple_rest.Security.ApplicationUser;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.UUID;

@Service
public class ApplicationUserDetailsService
        extends MappingSqlQuery<ApplicationUser>
        implements UserDetailsService {

    public static final String SQL = """
                    select * from application_user where username = :username
                    """;
    public ApplicationUserDetailsService(DataSource ds) {
        super(ds, SQL);
        this.declareParameter(new SqlParameter("username", Types.VARCHAR));
        this.compile();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.executeByNamedParam(Map.of("username", username))
                .stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    protected ApplicationUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ApplicationUser(rs.getObject("id", UUID.class),
                rs.getString("username"),
                rs.getString("password"));
    }
}
