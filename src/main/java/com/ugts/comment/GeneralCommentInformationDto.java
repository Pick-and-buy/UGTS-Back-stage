package com.ugts.comment;

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
}
