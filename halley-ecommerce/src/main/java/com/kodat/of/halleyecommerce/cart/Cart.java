package com.kodat.of.halleyecommerce.cart;

import com.kodat.of.halleyecommerce.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cartToken;

    @OneToMany(mappedBy = "cart" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id"  , unique = true)
    private User user;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


}
