package com.ugts.verifyid.service;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import com.ugts.verifyid.dto.VerifyInformationRequest;
import com.ugts.verifyid.entity.VerifyInformation;
import com.ugts.verifyid.repository.VerifyInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerifyInformationServiceImpl implements IVerifyInformation {
    private final UserRepository userRepository;
    private final VerifyInformationRepository verifyInformationRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    @Transactional
    public void handleVerifyUser(VerifyInformationRequest verifyInformationRequest) {
        User user = userRepository
                .findById(verifyInformationRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!verifyInformationRequest.getIsMatch()) {
            return;
        }
        try {
            if (!user.isVerified()) {
                user.setVerified(true);
                user.setUsername(verifyInformationRequest.getName());
                userRepository.save(user);
            }
            VerifyInformation verifyInformation = VerifyInformation.builder()
                    .user(user)
                    .cardId(passwordEncoder.encode(verifyInformationRequest.getCardId()))
                    .name(verifyInformationRequest.getName())
                    .dob(verifyInformationRequest.getDob())
                    .nationality(verifyInformationRequest.getNationality())
                    .home(verifyInformationRequest.getHome())
                    .address(verifyInformationRequest.getAddress())
                    .doe(verifyInformationRequest.getDoe())
                    .features(verifyInformationRequest.getFeatures())
                    .issueDate(verifyInformationRequest.getIssueDate())
                    .issueLoc(verifyInformationRequest.getIssueLoc())
                    .isMatch(verifyInformationRequest.getIsMatch())
                    .build();
            verifyInformationRepository.save(verifyInformation);
        } catch (Exception e) {
            throw new AppException(ErrorCode.VERIFY_FAIL);
        }
    }
}
