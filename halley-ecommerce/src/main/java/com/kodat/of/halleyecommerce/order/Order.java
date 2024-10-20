package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.order.enums.Status;
import com.kodat.of.halleyecommerce.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    private BigDecimal totalPrice;
    private Long addressId;
    @Enumerated(EnumType.STRING)
    private Status status;

}
