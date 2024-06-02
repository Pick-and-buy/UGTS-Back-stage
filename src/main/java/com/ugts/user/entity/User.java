package com.ugts.user.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String password;
    String lastName;
    String firstName;
    String email;
    String phoneNumber;
    LocalDate dob;

    @ManyToMany
    Set<Role> roles;
}
