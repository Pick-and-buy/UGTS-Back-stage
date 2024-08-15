package com.ugts.rating.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ugts.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String ratingId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StarRating stars;

    @NotBlank
    private String comment;

    // user duoc minh rate
    @ManyToOne
    @JoinColumn(name = "rating_user_id")
    private User ratingUser;

    // user rate minh
    @ManyToOne
    @JoinColumn(name = "rated_user_id")
    private User ratedUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Saigon")
    Date ratedAt;

    String orderId;
}
