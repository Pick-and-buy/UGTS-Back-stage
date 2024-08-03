package com.ugts.verifyid.service;

import java.text.SimpleDateFormat;
import java.util.Date;

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
            throw new AppException(ErrorCode.VERIFY_FAIL);
        }
        VerifyInformation userVerify = verifyInformationRepository.findByUser(user);
        if (passwordEncoder.matches(verifyInformationRequest.getCardId(), userVerify.getCardId())) {
            throw new AppException(ErrorCode.USER_HAS_ALREADY_VERIFIED);
        }
        try {
            changeUserInfo(user, verifyInformationRequest);
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

    private void changeUserInfo(User user, VerifyInformationRequest verifyInformationRequest) {
        try {
            if (!user.getIsVerified()) {
                String fullName = verifyInformationRequest.getName();
                user.setIsVerified(true);
                String[] nameParts = splitName(fullName);
                user.setLastName(nameParts[0]);
                user.setFirstName(nameParts[1]);
                user.setDob(convertStringToDate(verifyInformationRequest.getDob()));
                userRepository.save(user);
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.VERIFY_FAIL);
        }
    }

    public static String[] splitName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        // Tách chuỗi dựa vào dấu cách
        String[] parts = fullName.trim().split("\\s+");

        // Trường hợp chỉ có một phần tử
        if (parts.length == 1) {
            return new String[] {parts[0], ""};
        }

        // Trường hợp có nhiều phần tử
        String lastName = parts[0];
        String firstName = parts[parts.length - 1];
        return new String[] {lastName, firstName};
    }

    public Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateString);
        }
        return date;
    }
}
