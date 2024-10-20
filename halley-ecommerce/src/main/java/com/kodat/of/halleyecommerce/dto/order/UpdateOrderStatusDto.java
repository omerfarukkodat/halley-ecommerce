package com.kodat.of.halleyecommerce.dto.order;

import com.kodat.of.halleyecommerce.order.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusDto {
    @NotNull(message = "Status cannot be null")
    private Status status;
}
