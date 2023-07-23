package sg.edu.ntu.nutrimate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import sg.edu.ntu.nutrimate.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
  
    Optional<Customer> findByUserID(String userID);
    Optional<Customer> findByEmail(String email);
    void deleteByUserID(String userID);

}
