package com.ugts.verifyid.entity;


import com.ugts.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String verifyInfoId;

    @OneToOne
    User user;

    String cardId;

    String name;

    String dob;

    String nationality;

    String home;

    String address;

    String doe;

    String features;

    String issueDate;

    String issueLoc;

    Boolean isMatch;


}
