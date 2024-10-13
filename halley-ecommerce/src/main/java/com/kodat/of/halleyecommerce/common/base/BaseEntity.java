package com.kodat.of.halleyecommerce.common.base;

import com.kodat.of.halleyecommerce.common.base.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status" , nullable = false)
    private Status status = Status.ACTIVE;
}
