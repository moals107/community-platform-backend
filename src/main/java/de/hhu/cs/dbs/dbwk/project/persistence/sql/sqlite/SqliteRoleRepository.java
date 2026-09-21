package de.hhu.cs.dbs.dbwk.project.persistence.sql.sqlite;


import de.hhu.cs.dbs.dbwk.project.model.Role;
import de.hhu.cs.dbs.dbwk.project.model.RoleRepository;
import de.hhu.cs.dbs.dbwk.project.model.SimpleRole;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("roleRepository")
public class SqliteRoleRepository implements RoleRepository {

    @Override
    public Set<Role> findAllRoles() {
        return Set.of(
                new SimpleRole("BUERGER")
        );
    }
}

