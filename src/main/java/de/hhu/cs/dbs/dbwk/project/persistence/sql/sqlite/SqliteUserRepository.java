package de.hhu.cs.dbs.dbwk.project.persistence.sql.sqlite;

import de.hhu.cs.dbs.dbwk.project.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository("userRepository")
public class SqliteUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public SqliteUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findUser(String uniqueString) {
        String sql = "SELECT EMail, Passwort FROM Buerger WHERE EMail = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new Object[]{uniqueString}, (rs, rowNum) -> {
                String email = rs.getString("EMail");
                String password = rs.getString("Passwort");

                // Hier setzen wir fest, dass jeder Nutzer in der Buerger-Tabelle automatisch die Rolle "BUERGER" hat
                Set<Role> roles = new HashSet<>();
                roles.add(new SimpleRole("BUERGER"));

                return new SimpleUser(email, password, roles);
            });
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}