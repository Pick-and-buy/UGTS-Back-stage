package com.ugts.authentication.repository;

import java.util.Optional;

import com.ugts.authentication.entity.OtpViaEmail;
import com.ugts.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpViaEmailRepository extends JpaRepository<OtpViaEmail, Long> {

    @Query("SELECT o FROM OtpViaEmail o WHERE o.otpCode = ?1 AND o.user = ?2")
    Optional<OtpViaEmail> findByOtpAndUser(Integer otp, User user);
}
