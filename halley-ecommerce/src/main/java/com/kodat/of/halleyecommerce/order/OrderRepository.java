package com.kodat.of.halleyecommerce.order;

import com.kodat.of.halleyecommerce.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<List<Order>> findAllByUser(User user);
}
