package com.ugts.user.dto.request;

import java.time.LocalDate;

import com.ugts.user.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String username;
    String lastName;
    String firstName;
    String email;

    @DobConstraint(min = 14, message = "INVALID_DOB")
    LocalDate dob;
}
