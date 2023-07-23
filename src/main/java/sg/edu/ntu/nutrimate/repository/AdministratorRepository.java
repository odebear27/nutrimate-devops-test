package sg.edu.ntu.nutrimate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.ntu.nutrimate.entity.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    Optional<Administrator> findByUserID(String username);
    Optional<Administrator> findByEmail(String email);
}
