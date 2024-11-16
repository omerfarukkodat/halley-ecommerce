package com.kodat.of.halleyecommerce.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderDto {

   // @NotNull
    private Long addressId;
    private NonMemberInfoDto nonMemberInfoDto;
}
