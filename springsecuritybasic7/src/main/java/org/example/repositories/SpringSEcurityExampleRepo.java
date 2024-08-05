package org.example.repositories;

import org.example.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringSEcurityExampleRepo extends JpaRepository<User, String> {
    @Query(value = "SELECT u.* FROM \"user\" u WHERE u.username=?1",
            nativeQuery = true)
    User findUserByUserName(String username);
}
