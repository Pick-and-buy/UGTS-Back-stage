package com.ugts.order.dto.response;

import com.ugts.post.dto.GeneralPostInformationDto;
import com.ugts.user.dto.GeneralUserInformationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    String id;
    GeneralUserInformationDto buyer;
    GeneralPostInformationDto post;
    OrderDetailsResponse orderDetails;
}
