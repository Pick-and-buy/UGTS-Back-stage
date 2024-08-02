package com.ugts.verifyid.repository;

import com.ugts.user.entity.User;
import com.ugts.verifyid.entity.VerifyInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifyInformationRepository extends JpaRepository<VerifyInformation, String> {
    VerifyInformation findByUser(User user);
}
