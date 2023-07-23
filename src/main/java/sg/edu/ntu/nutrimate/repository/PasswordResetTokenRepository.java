package sg.edu.ntu.nutrimate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.entity.PasswordResetToken;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
    PasswordResetToken findByToken(String token);

    Optional<PasswordResetToken> findByCustomer(Customer customer);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

    void deleteByCustomer(Customer customer);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
