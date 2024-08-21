package com.ugts.comment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ugts.user.dto.GeneralUserInformationDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneralCommentInformationDto {
    GeneralUserInformationDto user;
    String id;
    String commentContent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date createAt;
}
