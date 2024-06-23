package org.example.repositories;

import org.example.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringSEcurityExampleRepo extends JpaRepository<User,String> {
}
